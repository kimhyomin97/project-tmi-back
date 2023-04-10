package com.tmi.controller;

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
    public ResponseEntity<List<Restaurant>> getNearestRestaurants(@RequestParam Double lat, @RequestParam Double lon) {
        List<Restaurant> nearestRestaurants = restaurantService.getNearestRestaurants(lat, lon);
        return ResponseEntity.ok().body(nearestRestaurants);
    }
}
