package com.tmi.controller;

import com.tmi.dto.Response;
import com.tmi.dto.Restaurant;
import com.tmi.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RestaurantController {

    private RestaurantService restaurantService;

    @GetMapping("/all")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.findAll();
    }

//    파라미터로 개수를 입력받아 해당 개수만큼 음식점 정보를 리턴하는 api
    @GetMapping("/count")
    public ResponseEntity<Response<List<Restaurant>>> getRestaurantsByCount(@RequestParam Integer count) {
        List<Restaurant> restaurants = restaurantService.findAll().subList(0, count);

        Response <List<Restaurant>> response = new Response<>();
        response.setMessage("음식점 목록 조회 성공");
        response.setData(restaurants);

        return ResponseEntity.ok().body(response);
    }

    // 페이지네이션이 적용된 음식점 정보를 리턴하는 api
    @GetMapping("/page")
    public ResponseEntity<Response<List<Restaurant>>> getRestaurantsByPage(@RequestParam Integer page, @RequestParam Integer size) {
        List<Restaurant> restaurants = restaurantService.findAll().subList(page * size, (page + 1) * size);

        Response <List<Restaurant>> response = new Response<>();
        response.setMessage("음식점 목록 조회 성공");
        response.setData(restaurants);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/near")
    public ResponseEntity<Response<List<Restaurant>>> getNearestRestaurants(@RequestParam Double lat, @RequestParam Double lon) {
        List<Restaurant> nearestRestaurants = restaurantService.getNearestRestaurants(lat, lon);

        Response <List<Restaurant>> response = new Response<>();
        response.setMessage("음식점 목록 조회 성공");
        response.setData(nearestRestaurants);

        return ResponseEntity.ok().body(response);
    }

    // call getNearByLatAndLon api
    @GetMapping("/nearby")
    public ResponseEntity<Response<List<Restaurant>>> getNearByLatAndLon(@RequestParam Double lat, @RequestParam Double lon, @RequestParam Integer limit) {
        List<Restaurant> nearByRestaurants = restaurantService.getNearByLatAndLon(lat, lon, limit);

        Response <List<Restaurant>> response = new Response<>();
        response.setMessage("음식점 목록 조회 성공");
        response.setData(nearByRestaurants);

        return ResponseEntity.ok().body(response);
    }

}
