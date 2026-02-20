# Project-TMI 장소 기록 기능 데이터베이스 설계

## 개요

사용자가 방문한 장소(음식점, 카페, 관광지 등)를 기록하고, 점수/태그 기반으로 지도 시각화 및 검색을 제공하는 기능의 데이터베이스 설계 문서이다.

### 핵심 설계 원칙

- 사용자 필수 입력은 **장소 선택 + 점수** 2개 동작으로 최소화
- 장소 기본 정보는 외부 API(카카오/구글)가 자동으로 채움
- 데이터 삭제는 soft delete(`deleted_at`) 방식 (Place, Visit)
- 태그 관련 테이블은 연결 해제 시 row 물리 삭제

### ERD

```
Place (1) ──── (N) Visit
  │                  │
 (N)                (N)
PlaceTag            VisitTag
 (N)                (N)
  │                  │
  └──── Tag (1) ─────┘
```

- Place : Visit = 1 : N (한 장소에 여러 번 방문)
- Place : Tag = N : M (PlaceTag 중간 테이블)
- Visit : Tag = N : M (VisitTag 중간 테이블)

---

## 테이블 상세

### 1. Place (장소)

장소의 기본 정보를 저장한다. 지도에서 장소를 선택하면 외부 API 데이터로 자동 생성되며, 사용자가 직접 입력하는 것은 `domain_type`(버튼 선택)뿐이다. 동일 장소는 `external_place_id + external_source` 조합으로 중복을 방지한다.

| 컬럼 | 타입 | 필수 | 기본값 | 입력 방식 | 설명 |
|---|---|---|---|---|---|
| id | BIGINT | O | AUTO_INCREMENT | 시스템 | PK |
| name | VARCHAR(100) | O | - | API 자동 | 장소명 |
| address | VARCHAR(255) | O | - | API 자동 | 도로명 주소 |
| latitude | DOUBLE | O | - | API 자동 | 위도 |
| longitude | DOUBLE | O | - | API 자동 | 경도 |
| domain_type | VARCHAR(20) | O | - | 사용자 버튼 선택 | RESTAURANT, CAFE, BAR, TRAVEL, ACCOMMODATION |
| category | VARCHAR(50) | - | NULL | API 자동 | 세부 분류 (일식, 한식, 자연, 문화 등) |
| external_place_id | VARCHAR(100) | O | - | API 자동 | 외부 API의 장소 고유 ID |
| external_source | VARCHAR(20) | O | - | 시스템 | 외부 API 출처 (KAKAO, GOOGLE) |
| created_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 시스템 | 생성일시 |
| updated_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 시스템 | 수정일시 |
| deleted_at | TIMESTAMP | - | NULL | 시스템 | 삭제일시 (soft delete) |

**제약조건:**
- PK: `id`
- UNIQUE: `(external_place_id, external_source)`
- `domain_type` 허용값: `RESTAURANT`, `CAFE`, `BAR`, `TRAVEL`, `ACCOMMODATION`

**DDL:**

```sql
CREATE TABLE place (
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    address         VARCHAR(255)    NOT NULL,
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    domain_type     VARCHAR(20)     NOT NULL,
    category        VARCHAR(50),
    external_place_id VARCHAR(100)  NOT NULL,
    external_source VARCHAR(20)     NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP,

    CONSTRAINT uq_place_external UNIQUE (external_place_id, external_source)
);

CREATE INDEX idx_place_domain_type ON place (domain_type);
CREATE INDEX idx_place_category ON place (category);
CREATE INDEX idx_place_location ON place (latitude, longitude);
```

**데이터 예시:**

| id | name | address | latitude | longitude | domain_type | category | external_place_id | external_source |
|---|---|---|---|---|---|---|---|---|
| 1 | 하카타분코 강남점 | 서울 강남구 테헤란로 123 | 37.5012 | 127.0396 | RESTAURANT | 일식 | 12345678 | KAKAO |
| 2 | 경복궁 | 서울 종로구 사직로 161 | 37.5796 | 126.9770 | TRAVEL | 문화 | 87654321 | KAKAO |
| 3 | 블루보틀 삼청점 | 서울 종로구 삼청로 76 | 37.5802 | 126.9822 | CAFE | 카페 | 11223344 | KAKAO |

---

### 2. Visit (방문 기록)

매 방문마다 하나의 레코드가 생성된다. 같은 Place에 여러 Visit이 가능하며, 방문 횟수는 `SELECT COUNT(*) FROM visit WHERE place_id = ?`로 조회한다. 필수 입력은 `score` 하나뿐이다.

| 컬럼 | 타입 | 필수 | 기본값 | 입력 방식 | 설명 |
|---|---|---|---|---|---|
| id | BIGINT | O | AUTO_INCREMENT | 시스템 | PK |
| place_id | BIGINT | O | - | 시스템 | FK → Place |
| score | SMALLINT | O | - | 사용자 별점 탭 | 1~5 |
| visited_at | DATE | O | CURRENT_DATE | 시스템 (변경 가능) | 방문일 |
| situation | VARCHAR(20) | - | NULL | 사용자 아이콘 탭 | 식사/방문 상황 |
| menu_items | VARCHAR(300) | - | NULL | 사용자 입력 | 먹은 메뉴 (콤마 구분) |
| price_per_person | INTEGER | - | NULL | 사용자 입력 | 1인당 가격 (원) |
| wait_time_minutes | SMALLINT | - | NULL | 사용자 입력 | 대기 시간 (분) |
| admission_fee | INTEGER | - | NULL | 사용자 입력 | 입장료 (원, 여행지용) |
| content | TEXT | - | NULL | 사용자 입력 | 자유 후기 |
| created_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 시스템 | 생성일시 |
| updated_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 시스템 | 수정일시 |
| deleted_at | TIMESTAMP | - | NULL | 시스템 | 삭제일시 (soft delete) |

**제약조건:**
- PK: `id`
- FK: `place_id` → `place(id)`
- CHECK: `score BETWEEN 1 AND 5`
- `situation` 허용값: `SOLO`, `FRIEND`, `COUPLE`, `FAMILY`, `BUSINESS`

**DDL:**

```sql
CREATE TABLE visit (
    id                  BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    place_id            BIGINT      NOT NULL,
    score               SMALLINT    NOT NULL,
    visited_at          DATE        NOT NULL DEFAULT CURRENT_DATE,
    situation           VARCHAR(20),
    menu_items          VARCHAR(300),
    price_per_person    INTEGER,
    wait_time_minutes   SMALLINT,
    admission_fee       INTEGER,
    content             TEXT,
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP,

    CONSTRAINT fk_visit_place FOREIGN KEY (place_id) REFERENCES place(id),
    CONSTRAINT chk_visit_score CHECK (score BETWEEN 1 AND 5)
);

CREATE INDEX idx_visit_place_id ON visit (place_id);
CREATE INDEX idx_visit_score ON visit (score);
CREATE INDEX idx_visit_visited_at ON visit (visited_at);
```

**데이터 예시:**

| id | place_id | score | visited_at | situation | menu_items | price_per_person | wait_time_minutes | admission_fee | content |
|---|---|---|---|---|---|---|---|---|---|
| 1 | 1 | 4 | 2025-01-15 | SOLO | 돈코츠라멘, 교자 | 12000 | 20 | NULL | 국물이 진하고 면발이 쫄깃함 |
| 2 | 1 | 3 | 2025-02-20 | FRIEND | 돈코츠라멘 | 12000 | 5 | NULL | 지난번보다 맛이 떨어진 느낌 |
| 3 | 2 | 5 | 2025-03-01 | COUPLE | NULL | NULL | NULL | 3000 | 봄 벚꽃 시즌에 방문, 최고였음 |

---

### 3. Tag (태그 마스터)

시스템에 존재하는 모든 태그의 정의 테이블이다. "혼밥가능"이라는 태그는 이 테이블에 1행만 존재하고, PlaceTag/VisitTag를 통해 여러 장소/방문에서 재사용된다. 태그는 삭제하지 않으며(마스터 데이터), 연결만 해제한다.

| 컬럼 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| id | BIGINT | O | AUTO_INCREMENT | PK |
| name | VARCHAR(30) | O | - | 태그명 (UNIQUE) |
| tag_category | VARCHAR(20) | O | - | 태그 분류 |
| source | VARCHAR(20) | O | - | 태그 생성 출처 |
| domain_type | VARCHAR(20) | - | NULL | NULL이면 전체 도메인 공통 |
| created_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 생성일시 |

**tag_category 허용값:**

| 값 | 설명 | 예시 |
|---|---|---|
| EXPERIENCE | 경험/분위기 | 조용함, 뷰맛집, 인스타감성 |
| CONVENIENCE | 편의시설 | 주차가능, 예약필수, 웨이팅있음 |
| COMPANION | 동행 관련 | 혼밥가능, 아이동반가능, 단체가능 |
| FOOD | 음식 특성 | 면요리, 매운맛, 가성비 |
| TRAVEL | 여행 특성 | 자연, 야간추천, 사진맛집 |
| CERTIFICATION | 외부 인증 | 블루리본, 미슐랭 |

**source 허용값:**

| 값 | 설명 | 예시 |
|---|---|---|
| PREDEFINED | 시스템 사전 정의 | 앱 초기 데이터로 제공하는 태그 |
| USER | 사용자 직접 생성 | 사용자가 커스텀 추가한 태그 |
| EXTERNAL | 외부 데이터 | 블루리본 API, 미슐랭 데이터 등 |
| SYSTEM | 시스템 자동 생성 | 방문 패턴 분석으로 자동 부여 |

**제약조건:**
- PK: `id`
- UNIQUE: `name`

**DDL:**

```sql
CREATE TABLE tag (
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(30)     NOT NULL,
    tag_category    VARCHAR(20)     NOT NULL,
    source          VARCHAR(20)     NOT NULL,
    domain_type     VARCHAR(20),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_tag_name UNIQUE (name)
);

CREATE INDEX idx_tag_category ON tag (tag_category);
CREATE INDEX idx_tag_domain_type ON tag (domain_type);
```

**데이터 예시 (사전 정의 태그):**

| id | name | tag_category | source | domain_type |
|---|---|---|---|---|
| 1 | 혼밥가능 | COMPANION | PREDEFINED | NULL |
| 2 | 주차가능 | CONVENIENCE | PREDEFINED | NULL |
| 3 | 면요리 | FOOD | PREDEFINED | RESTAURANT |
| 4 | 가성비 | EXPERIENCE | PREDEFINED | NULL |
| 5 | 사진맛집 | TRAVEL | PREDEFINED | TRAVEL |
| 6 | 블루리본2개 | CERTIFICATION | EXTERNAL | RESTAURANT |
| 7 | 미슐랭빕구르망 | CERTIFICATION | EXTERNAL | RESTAURANT |
| 8 | 야간추천 | TRAVEL | PREDEFINED | TRAVEL |
| 9 | 웨이팅있음 | CONVENIENCE | PREDEFINED | RESTAURANT |
| 10 | 아이동반가능 | COMPANION | PREDEFINED | NULL |

---

### 4. PlaceTag (장소-태그 연결)

장소 자체의 고정 속성을 태그로 연결하는 중간 테이블이다. 누가 방문하든, 언제 방문하든 변하지 않는 장소의 특성을 표현한다. (예: 주차가능, 블루리본2개, 역세권)

태그 해제 시 해당 row를 물리 삭제한다.

| 컬럼 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| id | BIGINT | O | AUTO_INCREMENT | PK |
| place_id | BIGINT | O | - | FK → Place |
| tag_id | BIGINT | O | - | FK → Tag |
| created_by | VARCHAR(20) | O | - | 태그 연결 주체 (USER, EXTERNAL, SYSTEM) |
| created_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 생성일시 |

**제약조건:**
- PK: `id`
- FK: `place_id` → `place(id)`, `tag_id` → `tag(id)`
- UNIQUE: `(place_id, tag_id)`

**DDL:**

```sql
CREATE TABLE place_tag (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    place_id    BIGINT      NOT NULL,
    tag_id      BIGINT      NOT NULL,
    created_by  VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_place_tag_place FOREIGN KEY (place_id) REFERENCES place(id),
    CONSTRAINT fk_place_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id),
    CONSTRAINT uq_place_tag UNIQUE (place_id, tag_id)
);

CREATE INDEX idx_place_tag_place_id ON place_tag (place_id);
CREATE INDEX idx_place_tag_tag_id ON place_tag (tag_id);
```

**데이터 예시:**

| id | place_id | tag_id | created_by | 의미 |
|---|---|---|---|---|
| 1 | 1 | 2 | USER | 하카타분코 - 주차가능 |
| 2 | 1 | 9 | USER | 하카타분코 - 웨이팅있음 |
| 3 | 1 | 6 | EXTERNAL | 하카타분코 - 블루리본2개 |

---

### 5. VisitTag (방문-태그 연결)

특정 방문에서의 경험을 태그로 연결하는 중간 테이블이다. 같은 장소라도 방문마다 다른 태그가 붙을 수 있다. (예: 첫 방문은 #혼밥 #면요리, 두 번째 방문은 #회식 #교자)

태그 해제 시 해당 row를 물리 삭제한다.

| 컬럼 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| id | BIGINT | O | AUTO_INCREMENT | PK |
| visit_id | BIGINT | O | - | FK → Visit |
| tag_id | BIGINT | O | - | FK → Tag |
| created_by | VARCHAR(20) | O | - | 태그 연결 주체 (USER, SYSTEM) |
| created_at | TIMESTAMP | O | CURRENT_TIMESTAMP | 생성일시 |

**제약조건:**
- PK: `id`
- FK: `visit_id` → `visit(id)`, `tag_id` → `tag(id)`
- UNIQUE: `(visit_id, tag_id)`

**DDL:**

```sql
CREATE TABLE visit_tag (
    id          BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    visit_id    BIGINT      NOT NULL,
    tag_id      BIGINT      NOT NULL,
    created_by  VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_visit_tag_visit FOREIGN KEY (visit_id) REFERENCES visit(id),
    CONSTRAINT fk_visit_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id),
    CONSTRAINT uq_visit_tag UNIQUE (visit_id, tag_id)
);

CREATE INDEX idx_visit_tag_visit_id ON visit_tag (visit_id);
CREATE INDEX idx_visit_tag_tag_id ON visit_tag (tag_id);
```

**데이터 예시:**

| id | visit_id | tag_id | created_by | 의미 |
|---|---|---|---|---|
| 1 | 1 | 1 | USER | 1/15 방문 - 혼밥가능 |
| 2 | 1 | 3 | USER | 1/15 방문 - 면요리 |
| 3 | 1 | 4 | USER | 1/15 방문 - 가성비 |
| 4 | 2 | 3 | USER | 2/20 방문 - 면요리 |

---

## 주요 쿼리 패턴

### 특정 장소의 방문 횟수

```sql
SELECT COUNT(*)
FROM visit
WHERE place_id = :placeId
  AND deleted_at IS NULL;
```

### 점수 기반 지도 표시 (4점 이상만)

```sql
SELECT p.id, p.name, p.latitude, p.longitude, p.domain_type,
       AVG(v.score) AS avg_score,
       COUNT(v.id) AS visit_count
FROM place p
JOIN visit v ON p.id = v.place_id AND v.deleted_at IS NULL
WHERE p.deleted_at IS NULL
GROUP BY p.id, p.name, p.latitude, p.longitude, p.domain_type
HAVING AVG(v.score) >= 4;
```

### 태그 기반 검색 (혼밥 + 면요리 + 4점 이상)

```sql
SELECT DISTINCT p.id, p.name, p.latitude, p.longitude
FROM place p
JOIN visit v ON p.id = v.place_id AND v.deleted_at IS NULL
JOIN visit_tag vt ON v.id = vt.visit_id
JOIN tag t ON vt.tag_id = t.id
WHERE p.deleted_at IS NULL
  AND t.name IN ('혼밥가능', '면요리')
  AND v.score >= 4
GROUP BY p.id, p.name, p.latitude, p.longitude
HAVING COUNT(DISTINCT t.name) = 2;
```

### 블루리본 등재 장소 조회 (외부 데이터 태그 활용)

```sql
SELECT p.id, p.name, t.name AS certification
FROM place p
JOIN place_tag pt ON p.id = pt.place_id
JOIN tag t ON pt.tag_id = t.id
WHERE p.deleted_at IS NULL
  AND t.tag_category = 'CERTIFICATION'
  AND t.name LIKE '블루리본%';
```

### 월별 방문 통계

```sql
SELECT TO_CHAR(v.visited_at, 'YYYY-MM') AS month,
       COUNT(*) AS visit_count,
       ROUND(AVG(v.score), 1) AS avg_score
FROM visit v
WHERE v.deleted_at IS NULL
GROUP BY TO_CHAR(v.visited_at, 'YYYY-MM')
ORDER BY month DESC;
```

---

## 초기 시드 데이터 (사전 정의 태그)

애플리케이션 최초 실행 시 아래 태그를 `tag` 테이블에 INSERT한다.

```sql
-- 공통 태그 (domain_type = NULL)
INSERT INTO tag (name, tag_category, source, domain_type) VALUES
('혼밥가능', 'COMPANION', 'PREDEFINED', NULL),
('아이동반가능', 'COMPANION', 'PREDEFINED', NULL),
('단체가능', 'COMPANION', 'PREDEFINED', NULL),
('반려동물가능', 'COMPANION', 'PREDEFINED', NULL),
('데이트', 'COMPANION', 'PREDEFINED', NULL),
('주차가능', 'CONVENIENCE', 'PREDEFINED', NULL),
('주차불가', 'CONVENIENCE', 'PREDEFINED', NULL),
('예약필수', 'CONVENIENCE', 'PREDEFINED', NULL),
('웨이팅있음', 'CONVENIENCE', 'PREDEFINED', NULL),
('역세권', 'CONVENIENCE', 'PREDEFINED', NULL),
('조용함', 'EXPERIENCE', 'PREDEFINED', NULL),
('분위기좋음', 'EXPERIENCE', 'PREDEFINED', NULL),
('뷰맛집', 'EXPERIENCE', 'PREDEFINED', NULL),
('가성비', 'EXPERIENCE', 'PREDEFINED', NULL),
('인스타감성', 'EXPERIENCE', 'PREDEFINED', NULL);

-- 음식점 전용 태그 (domain_type = 'RESTAURANT')
INSERT INTO tag (name, tag_category, source, domain_type) VALUES
('면요리', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('국물요리', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('고기요리', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('해산물', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('매운맛', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('양많음', 'FOOD', 'PREDEFINED', 'RESTAURANT'),
('늦게까지영업', 'CONVENIENCE', 'PREDEFINED', 'RESTAURANT'),
('포장가능', 'CONVENIENCE', 'PREDEFINED', 'RESTAURANT');

-- 여행지 전용 태그 (domain_type = 'TRAVEL')
INSERT INTO tag (name, tag_category, source, domain_type) VALUES
('자연', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('문화', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('역사', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('액티비티', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('사진맛집', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('산책좋음', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('야간추천', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('무료', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('봄추천', 'TRAVEL', 'PREDEFINED', 'TRAVEL'),
('가을추천', 'TRAVEL', 'PREDEFINED', 'TRAVEL');

-- 카페 전용 태그 (domain_type = 'CAFE')
INSERT INTO tag (name, tag_category, source, domain_type) VALUES
('작업하기좋음', 'EXPERIENCE', 'PREDEFINED', 'CAFE'),
('디저트맛집', 'FOOD', 'PREDEFINED', 'CAFE'),
('콘센트있음', 'CONVENIENCE', 'PREDEFINED', 'CAFE'),
('넓음', 'EXPERIENCE', 'PREDEFINED', 'CAFE');
```

---

## 설계 메모

### Soft Delete 정책
- `Place`, `Visit`: `deleted_at IS NULL` 조건을 모든 조회 쿼리에 포함
- `PlaceTag`, `VisitTag`: row 물리 삭제 (soft delete 미적용)
- `Tag`: 삭제하지 않음 (마스터 데이터)
- Place 삭제 시 관련 Visit은 애플리케이션 레벨에서 함께 soft delete 처리

### 도메인별 필드 전략
- `wait_time_minutes`: 음식점(RESTAURANT) 방문 시 선택 입력
- `admission_fee`: 여행지(TRAVEL) 방문 시 선택 입력
- 프론트엔드에서 `Place.domain_type`에 따라 해당 필드만 표시
- 도메인이 5개 이상으로 늘어나면 별도 상세 테이블 분리 검토

### 태그 입력 UX 전략
- `Place.domain_type`과 `Place.category` 기반으로 관련 태그 5~8개 추천
- 사용자는 추천 태그를 탭하여 선택/해제
- "+" 버튼으로 커스텀 태그 추가 가능 (기존 태그 자동완성으로 중복 방지)
- PlaceTag: 장소 저장 시 장소 속성 태그 선택
- VisitTag: 방문 저장 시 경험 태그 선택

### 확장 계획
- 블루리본/미슐랭 데이터 확보 시 `PlaceTag`에 `created_by = 'EXTERNAL'`로 추가
- 방문 패턴 분석 시스템 태그는 `VisitTag`에 `created_by = 'SYSTEM'`으로 자동 부여
- 사진 기능은 `VisitPhoto` 테이블 추가로 대응 (MVP 이후)
