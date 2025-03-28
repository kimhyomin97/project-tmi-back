package com.tmi.controller;

import com.tmi.dto.ResponseDto;
import com.tmi.dto.Restaurant;
import com.tmi.dto.ReviewRequestDto;
import com.tmi.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;

//    전체 데이터 조회시 과부하
//    @GetMapping("/all")
//    @LogExecutionTime
//    public List<Restaurant> getAllRestaurants() {
//        log.info("request all restaurants");
//        return restaurantService.findAll();
//    }
    @GetMapping
    public ResponseEntity<ResponseDto<List<Restaurant>>> getRestaurants(
            @RequestParam(required = false) String category,
            @RequestParam(required = true) Double lat,
            @RequestParam(required = true) Double lng,
            @RequestParam(required = false, defaultValue = "100") int radius
    ) {
        List<Restaurant> restaurants = restaurantService.getRestaurants(category, lat, lng, radius);
        ResponseDto<List<Restaurant>> responseDto = new ResponseDto<>();
        responseDto.setMessage("음식점 조회 성공");
        responseDto.setData(restaurants);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/reviews")
    public ResponseEntity<ResponseDto<Void>> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        log.info("create review userId={}", reviewRequestDto.getMemberId());
        restaurantService.createReview(reviewRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseDto<Void>> deleteReview(@PathVariable Long reviewId) {
        log.info("delete review reviewId={}", reviewId);
        restaurantService.deleteReview(reviewId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 파라미터로 개수를 입력받아 해당 개수만큼 음식점 정보를 리턴하는 api
     * @param count 음식점 개수
     * */
    @GetMapping("/count")
    public ResponseEntity<ResponseDto<List<Restaurant>>> getRestaurantsByCount(@RequestParam Integer count) {
        log.info("request restaurants by count: count={}", count);
        List<Restaurant> restaurants = restaurantService.findAll().subList(0, count);

        ResponseDto<List<Restaurant>> responseDto = new ResponseDto<>();
        responseDto.setMessage("음식점 목록 조회 성공");
        responseDto.setData(restaurants);

        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 페이지네이션이 적용된 음식점 정보를 리턴하는 api
     * @param page 페이지 번호
     * @param size 페이지 크기
     * */
    @GetMapping("/page")
    public ResponseEntity<ResponseDto<List<Restaurant>>> getRestaurantsByPage(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("request restaurants by page: page={}, size={}", page, size);
        List<Restaurant> restaurants = restaurantService.findAll().subList(page * size, (page + 1) * size);

        ResponseDto<List<Restaurant>> responseDto = new ResponseDto<>();
        responseDto.setMessage("음식점 목록 조회 성공");
        responseDto.setData(restaurants);

        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 좌표를 입력받아 해당 좌표 근처의 음식점 정보를 리턴하는 api
     * @param lat 위도
     *            ( -90.0 <= lat <= 90.0 )
     * @param lon 경도
     *            ( -180.0 <= lon <= 180.0 )
     * */
    @GetMapping("/near")
    public ResponseEntity<ResponseDto<List<Restaurant>>> getNearestRestaurants(@RequestParam Double lat, @RequestParam Double lon) {
        log.info("request restaurants near lat={}, lon={}", lat, lon);
        List<Restaurant> nearestRestaurants = restaurantService.getNearestRestaurants(lat, lon);

        ResponseDto<List<Restaurant>> responseDto = new ResponseDto<>();
        responseDto.setMessage("음식점 목록 조회 성공");
        responseDto.setData(nearestRestaurants);

        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 좌표를 입력받아 해당 좌표 근처의 음식점 정보를 리턴하는 api
     * @param lat 위도
     *            ( -90.0 <= lat <= 90.0 )
     * @param lon 경도
     *            ( -180.0 <= lon <= 180.0 )
     * */
    @GetMapping("/nearby")
    public ResponseEntity<ResponseDto<List<Restaurant>>> getNearByLatAndLon(@RequestParam Double lat, @RequestParam Double lon, @RequestParam Integer limit) {
        log.info("request restaurants nearby lat={}, lon={}, limit={}", lat, lon, limit);
        List<Restaurant> nearByRestaurants = restaurantService.getNearByLatAndLon(lat, lon, limit);

        ResponseDto<List<Restaurant>> responseDto = new ResponseDto<>();
        responseDto.setMessage("음식점 목록 조회 성공");
        responseDto.setData(nearByRestaurants);

        return ResponseEntity.ok().body(responseDto);
    }

}
