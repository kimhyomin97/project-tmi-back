package com.tmi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusInfoResponse {

    @Getter
    @Setter
    public static class BusInfo {
        @JsonProperty("RTE_NM")
        private String routName;
        @JsonProperty("RTE_ID")
        private String routeId;
    }

    @Getter
    @Setter
    public static class BusRoute {
        @JsonProperty("list_total_count")
        private int listTotalCount;

        @JsonProperty("RESULT")
        private RESULT result;
        private List<BusInfo> row;
    }

    @Getter
    @Setter
    public static class RESULT {
        @JsonProperty("CODE")
        private String CODE;
        @JsonProperty("MESSAGE")
        private String MESSAGE;
    }

    private BusRoute busRoute;
}
