package com.tmi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

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
}
