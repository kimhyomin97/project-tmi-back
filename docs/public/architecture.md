# Project-TMI 아키텍처 설계

## 아키텍처

레이어드 아키텍처 기반. 외부 API 연동부만 인터페이스로 분리하여 의존성 역전 원칙을 적용한다.

## 패키지 구조

도메인별 패키지 구조를 사용한다. 변경의 단위가 도메인 단위로 발생하므로, 기능 수정 시 단일 패키지 내에서 작업이 완결된다.

```
com.tmi
├── place/
│   ├── controller/        PlaceController
│   ├── service/           PlaceService
│   ├── repository/        PlaceRepository
│   ├── entity/            Place (JPA Entity)
│   └── dto/               CreatePlaceRequest, PlaceResponse, PlaceMapResponse
│
├── visit/
│   ├── controller/        VisitController
│   ├── service/           VisitService
│   ├── repository/        VisitRepository
│   ├── entity/            Visit
│   └── dto/               CreateVisitRequest, VisitResponse, VisitStatsResponse
│
├── tag/
│   ├── controller/        TagController
│   ├── service/           TagService, PlaceTagService, VisitTagService
│   ├── repository/        TagRepository, PlaceTagRepository, VisitTagRepository
│   ├── entity/            Tag, PlaceTag, VisitTag
│   └── dto/               TagResponse, AttachTagRequest
│
├── external/
│   ├── PlaceSearchService.java          인터페이스
│   ├── PlaceSearchResult.java           외부 API 공통 응답 DTO
│   └── kakao/
│       ├── KakaoPlaceSearchService.java 구현체
│       ├── KakaoApiProperties.java      설정값 (@ConfigurationProperties)
│       └── dto/
│           └── KakaoSearchResponse.java Kakao API 원본 응답 매핑
│
└── global/
    ├── config/            WebConfig, MapStructConfig
    ├── exception/         BusinessException, ErrorCode, ErrorResponse, GlobalExceptionHandler
    └── response/          ApiResponse<T>
```

## API 설계 표준

Base path: `/api/v1`

### URL 구조

```
/api/{version}/{resource}/{resourceId}
```

- **version**: API 버전. 하위 호환이 깨지는 변경 시 증가한다.
- **resource**: 도메인 리소스명. 복수형 명사만 사용하고, 동사는 허용하지 않는다.
- **resourceId**: 리소스 고유 식별자. Path Variable로 전달한다.

```
GET    /api/v1/places                         장소 목록
POST   /api/v1/places                         장소 생성
GET    /api/v1/places/{placeId}               장소 상세
PATCH  /api/v1/visits/{visitId}               방문 수정
DELETE /api/v1/visits/{visitId}               방문 삭제
GET    /api/v1/tags                           태그 목록
```

상위 리소스에 소속된 행위는 하위에 sub-resource를 둔다.

```
POST   /api/v1/places/{placeId}/visits        장소에 방문 생성
GET    /api/v1/places/{placeId}/visits        장소의 방문 목록
POST   /api/v1/places/{placeId}/tags          장소에 태그 연결
DELETE /api/v1/places/{placeId}/tags/{tagId}   장소-태그 해제
```

### HTTP 메서드

| 메서드 | 용도 | 멱등성 | 요청 본문 |
|--------|------|--------|-----------|
| GET | 조회 | O | 없음 (Query Parameter로 필터링) |
| POST | 생성 | X | 있음 |
| PATCH | 부분 수정 | O | 변경할 필드만 포함 |
| DELETE | 삭제 | O | 없음 |

PUT은 사용하지 않는다. 이 프로젝트에서 리소스 전체 교체가 필요한 경우가 없고, 부분 수정(PATCH)으로 충분하다.

### 상태 코드

| 코드 | 의미 | 사용 시점 |
|------|------|-----------|
| 200 | OK | 조회, 수정 성공 |
| 201 | Created | 리소스 생성 성공 |
| 204 | No Content | 삭제 성공 |
| 400 | Bad Request | 입력값 유효성 실패 |
| 404 | Not Found | 리소스 없음 |
| 409 | Conflict | 중복 등록, 이미 연결된 태그 |
| 502 | Bad Gateway | 외부 API 호출 실패 |

### 네이밍 규칙

- URL: kebab-case (`/visit-stats/monthly`)
- Query Parameter: camelCase (`?domainType=RESTAURANT&minScore=4`)
- Request/Response Body: camelCase
- Enum 값: UPPER_SNAKE_CASE (`RESTAURANT`, `SOLO`)

### 목록 조회 표준

필터링은 Query Parameter, 페이징은 Spring 기본 Pageable을 사용한다.

```
GET /api/v1/places?domainType=RESTAURANT&minScore=4&tags=혼밥가능,가성비&page=0&size=20&sort=createdAt,desc
```

## 공통 응답 형식

```json
// 성공
{
  "status": "SUCCESS",
  "data": { },
  "error": null
}

// 실패
{
  "status": "ERROR",
  "data": null,
  "error": {
    "code": "PLACE_NOT_FOUND",
    "message": "존재하지 않는 장소입니다."
  }
}

// 페이징
{
  "status": "SUCCESS",
  "data": {
    "content": [ ],
    "page": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8
  },
  "error": null
}
```

`ApiResponse<T>` 제네릭 클래스로 구현한다.

## 예외 처리

`@RestControllerAdvice` 기반 글로벌 예외 핸들러를 사용한다.
단일 `BusinessException` + `ErrorCode` enum 방식으로, 새 에러 추가 시 enum 한 줄만 추가하면 된다.

### ErrorCode enum

```java
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Place
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장소입니다."),
    DUPLICATE_PLACE(HttpStatus.CONFLICT, "이미 등록된 장소입니다."),

    // Visit
    VISIT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 방문 기록입니다."),
    INVALID_SCORE(HttpStatus.BAD_REQUEST, "점수는 1~5 사이여야 합니다."),

    // Tag
    TAG_ALREADY_ATTACHED(HttpStatus.CONFLICT, "이미 연결된 태그입니다."),

    // External API
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "외부 API 호출에 실패했습니다."),

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
```

### BusinessException

```java
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

### 사용

```java
throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
```

### 파일 구조

```
global/exception/
├── BusinessException.java
├── ErrorCode.java
├── ErrorResponse.java
└── GlobalExceptionHandler.java
```

예외 클래스를 도메인마다 분리하지 않는다. 예외별 고유 로직이 없으므로 ErrorCode enum으로 중앙 관리한다.

## DTO 변환

MapStruct를 사용한다. 컴파일 타임 코드 생성 방식으로 타입 안전성과 성능을 확보한다.

```java
@Mapper(componentModel = "spring")
public interface PlaceMapper {
    PlaceResponse toResponse(Place place);
    PlaceMapResponse toMapResponse(Place place);
    Place toEntity(CreatePlaceRequest request);
}
```

### DTO 분리 원칙

- 요청(Request)과 응답(Response)은 반드시 분리
- 용도가 다르면 응답도 분리 (PlaceResponse ≠ PlaceMapResponse)
- Entity를 Controller 밖으로 노출하지 않음

## 외부 API 연동

인터페이스로 분리하여 구현체 교체가 가능한 구조를 유지한다.

```java
public interface PlaceSearchService {
    List<PlaceSearchResult> search(String keyword, double lat, double lng);
}

@Service
@Primary
public class KakaoPlaceSearchService implements PlaceSearchService { ... }

// 추후 확장
// public class GooglePlaceSearchService implements PlaceSearchService { ... }
```

API Key 등 설정값은 `@ConfigurationProperties`로 관리하고, `application.yml`에서 주입한다.

## 테스트 전략

| 계층 | 방식 | 도구 | 검증 대상 |
|------|------|------|-----------|
| Service | 단위 테스트 | Mockito | 비즈니스 로직, 예외 발생 조건 |
| Repository | 통합 테스트 | @DataJpaTest + Testcontainers | 쿼리 정합성, N+1 검증 |
| Controller | 슬라이스 테스트 | @WebMvcTest + MockMvc | 요청 유효성 검증, 응답 형식 |
| 외부 API | Mock 테스트 | WireMock | 외부 API 장애 시 예외 처리 |

Testcontainers로 PostgreSQL 컨테이너를 사용하여 실제 DB 환경과 동일한 조건에서 테스트한다.
