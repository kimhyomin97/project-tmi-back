package com.tmi.controller;//import com.tmi.controller.RestaurantController;
import com.tmi.controller.RestaurantController;
import com.tmi.dto.Restaurant;
import com.tmi.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestaurantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    public void testGetAllRestaurants() throws Exception {
        Restaurant restaurant1 = new Restaurant(
                LocalDateTime.parse("2023-07-29T12:34:56"),
                "Address 1",
                37.12345,
                "2023-07-29T10:00:00",
                127.56789,
                "Restaurant 1",
                "Korean",
                "Open",
                "12345",
                "http://www.restaurant1.com",
                202722.53183055,
                444055.07069569,
                LocalDateTime.parse("2023-07-29T12:34:56")
        );

        Restaurant restaurant2 = new Restaurant(
                LocalDateTime.parse("2023-07-29T12:34:57"),
                "Address 2",
                37.23456,
                "2023-07-29T09:30:00",
                127.67890,
                "Restaurant 2",
                "Italian",
                "Open",
                "56789",
                "http://www.restaurant2.com",
                203011.67895432,
                445566.12345678,
                LocalDateTime.parse("2023-07-29T12:34:57")
        );

        List<Restaurant> allRestaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantService.findAll()).thenReturn(allRestaurants);

        // When and Then
        mockMvc.perform(get("/api/restaurants/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"))
                .andExpect(jsonPath("$[1].address").value("Address 2"));
    }

    @Test
    public void testGetNearestRestaurants() throws Exception {
        Restaurant restaurant1 = new Restaurant(
                LocalDateTime.parse("2023-07-29T12:34:56"),
                "Address 1",
                37.12345,
                "2023-07-29T10:00:00",
                127.56789,
                "Restaurant 1",
                "Korean",
                "Open",
                "12345",
                "http://www.restaurant1.com",
                202722.53183055,
                444055.07069569,
                LocalDateTime.parse("2023-07-29T12:34:56")
        );

        Restaurant restaurant2 = new Restaurant(
                LocalDateTime.parse("2023-07-29T12:34:57"),
                "Address 2",
                37.23456,
                "2023-07-29T09:30:00",
                127.67890,
                "Restaurant 2",
                "Italian",
                "Open",
                "56789",
                "http://www.restaurant2.com",
                203011.67895432,
                445566.12345678,
                LocalDateTime.parse("2023-07-29T12:34:57")
        );

        List<Restaurant> nearestRestaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantService.getNearestRestaurants(anyDouble(), anyDouble())).thenReturn(nearestRestaurants);

        // When and Then
        mockMvc.perform(get("/api/restaurants/near")
                        .param("lat", "37.12345")
                        .param("lon", "127.56789"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[0].address").value("Address 1"))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"))
                .andExpect(jsonPath("$[1].address").value("Address 2"));
    }
}
