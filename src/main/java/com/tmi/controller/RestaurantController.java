package com.tmi.controller;

import com.tmi.dto.Response;
import com.tmi.dto.Restaurant;
import com.tmi.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin("*")
public class RestaurantController {

    private RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/all")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/near")
    public ResponseEntity<Response<List<Restaurant>>> getNearestRestaurants(@RequestParam Double lat, @RequestParam Double lon) {
        List<Restaurant> nearestRestaurants = restaurantService.getNearestRestaurants(lat, lon);

        Response <List<Restaurant>> response = new Response<>();
        response.setMessage("음식점 목록 조회 성공");
        response.setData(nearestRestaurants);

        return ResponseEntity.ok().body(response);
    }
}
