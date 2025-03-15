package com.tmi.service;

import com.tmi.dto.Restaurant;
import com.tmi.dto.ReviewRequestDto;
import com.tmi.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RestaurantService {
    public List<Restaurant> getRestaurants(String category, Double lat, Double lng, int radius);

    public void createReview(ReviewRequestDto reviewRequestDto);

    public void deleteReview(Long reviewId);
    public List<Restaurant> findAll();

    public List<Restaurant> findRestaurantsByName(String name);

    public List<Restaurant> getNearestRestaurants(Double lat, Double lon);

    public List<Restaurant> getNearByLatAndLon(Double lat, Double lon, int limit);

    public List<Restaurant> getApiTest();
}
