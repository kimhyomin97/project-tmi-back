package com.tmi.service;

import com.tmi.dto.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@WebAppConfiguration
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Test
    public void getApiTest(){
        restaurantService.getApiTest();
    }

    @Test
    public void getResturantsTest(){
        List<Restaurant> restaurants = restaurantService.getRestaurants(null, 37.5665, 126.9780, 500);
        int count = 0;
        for(Restaurant restaurant : restaurants) {
            if(count++ == 10) break;
            System.out.println(restaurant.getName());
        }
    }
}
