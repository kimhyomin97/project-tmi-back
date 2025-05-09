package com.tmi.service;

import com.tmi.dto.Member;
import com.tmi.dto.Restaurant;
import com.tmi.dto.ReviewRequestDto;
import com.tmi.entity.Review;
import com.tmi.exception.CustomErrorCode;
import com.tmi.exception.CustomException;
import com.tmi.repository.MemberRepository;
import com.tmi.repository.RestaurantRepository;
import com.tmi.repository.ReviewRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    @Value("${datagokr.apikeyInfuser}")
    private String apiKey;

    @Value("${datagokr.apiUrl}")
    private String apiUrl;

    private final RestaurantRepository restaurantRepository;

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    private final MeterRegistry meterRegistry;

    private final AtomicInteger reviewCount = new AtomicInteger(0);

    @PostConstruct
    public void registerGauge() {
        Gauge.builder("review.count", reviewCount, AtomicInteger::get)
                .description("등록된 review 개수")
                .register(meterRegistry);
    }

    public List<Restaurant> getRestaurants(String category, Double lat, Double lng, int radius) {
        return restaurantRepository.findNearByWithCategory(lat, lng, radius, category);
    }

    public void createReview(ReviewRequestDto reviewRequestDto) {

        Member member = memberRepository.findById(reviewRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Restaurant restaurant = restaurantRepository.findById(reviewRequestDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식점입니다."));

        Review review = Review.builder()
                .restaurant(restaurant)
                .memberId(reviewRequestDto.getMemberId())
                .rating(reviewRequestDto.getRating())
                .comment(reviewRequestDto.getComment())
                .build();

        reviewRepository.save(review);
        reviewCount.incrementAndGet();
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다"));
        if(review.getDeleteDttm() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 리뷰입니다.");
        }

        review.deleteReview();
        reviewCount.decrementAndGet();
    }
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> findRestaurantsByName(String name) {
        return restaurantRepository.findByName(name);
    }

    public List<Restaurant> getNearestRestaurants(Double lat, Double lon) {
        return restaurantRepository.findNearestRestaurants(lat, lon);
    }

    public List<Restaurant> getNearByLatAndLon(Double lat, Double lon, int limit) {
        // 유효성 검사: lat과 lon이 null이거나 범위를 벗어나면 예외 처리
        if (lat == null || lon == null || lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0) {
            log.error("Invalid lat and lon: lat={}, lon={}", lat, lon);
            throw new CustomException(CustomErrorCode.INVALID_LATLON);
        }
        return restaurantRepository.findNearByLatAndLon(lat, lon, limit);
    }

    public List<Restaurant> getApiTest(){
        List<Restaurant> temp = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl+"/15098046/v1/uddi:086910c1-c1eb-4a9a-98df-910ed0495f6b?page=1&perPage=10", HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());

        return temp;
    }

}
