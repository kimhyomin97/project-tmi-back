package com.tmi.service;

import com.tmi.dto.TransportDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@Service
public class MapServiceImpl implements MapService{

    private String apiUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid";

    @Value("${datagokr.apikey}")
    private String apiKey;

    public void getBustPositionInfo(TransportDto transportDto) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder urlBuilder = new StringBuilder(apiUrl);

        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey); // TODO : key 인증실패 -> 인증서버 key 동기화 이후 다시 확인 필요
        urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode("103900011", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("startOrd", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("endOrd", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));

        ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);


        System.out.println(response);
    }
}
