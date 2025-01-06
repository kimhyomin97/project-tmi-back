package com.tmi.service;

import com.tmi.dto.Restaurant;
import com.tmi.exception.CustomErrorCode;
import com.tmi.exception.CustomException;
import com.tmi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    @Value("${datagokr.apikey}")
    private String apiKey;

    @Value("${datagokr.apiUrl}")
    private String apiUrl;

    private final RestaurantRepository restaurantRepository;
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
