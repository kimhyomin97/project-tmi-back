package com.tmi.service;

import com.tmi.dto.TransportDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@WebAppConfiguration
public class MapServiceTest {

    @Autowired
    MapService mapService;

    @Test
    public void getBustPositionInfoTest() throws Exception {
        TransportDto temp = new TransportDto();
        mapService.getBustPositionInfo(temp);
    }
}
