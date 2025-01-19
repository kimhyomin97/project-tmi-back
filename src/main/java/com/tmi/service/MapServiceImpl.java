package com.tmi.service;

import com.tmi.dto.BusInfoResponse;
import com.tmi.dto.TransportDto;
import com.tmi.entity.BusIdentification;
import com.tmi.repository.BusIdentificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService{

    private String apiUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid";

    @Value("${dataseoul.apiurl}")
    private String dataSeoulApiUrl;

    @Value("${dataseoul.apikey}")
    private String dataSeoulApiKey;

    private final RestTemplate restTemplate;

    @Value("${datagokr.apikey}")
    private String apiKey;

    @Autowired
    BusIdentificationRepository busIdentificationRepository;

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


     public void getBusInfo() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<BusInfoResponse> response = restTemplate.exchange(dataSeoulApiUrl + "/" + dataSeoulApiKey + "/json/busRoute/1/1000/", HttpMethod.GET, null, BusInfoResponse.class);

        BusInfoResponse busInfoResponse = response.getBody();

        if (busInfoResponse != null && busInfoResponse.getBusRoute() != null) {
            List<BusInfoResponse.BusInfo> busInfoList = busInfoResponse.getBusRoute().getRow();

            for (BusInfoResponse.BusInfo busInfo : busInfoList) {
                if(!busIdentificationRepository.existsByBusIdAndDelDttmIsNull(busInfo.getRouteId())){
                    BusIdentification busIdentification = new BusIdentification();
                    busIdentification.setBusName(busInfo.getRoutName());
                    busIdentification.setBusId(busInfo.getRouteId());
                    busIdentification.setRegDttm(LocalDateTime.now());

                    busIdentificationRepository.save(busIdentification);
                }
            }
        }
     }
}
