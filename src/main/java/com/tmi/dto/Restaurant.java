package com.tmi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String licenseDttm;
    private String name;
    private String address;
    private String startDttm;
    private String restType;
    private Double lat;
    private Double lon;

    public Restaurant() {}

    public Restaurant(String licenseDttm, String name, String address, String startDttm, String restType, Double lat, Double lon) {
        this.licenseDttm = licenseDttm;
        this.name = name;
        this.address = address;
        this.startDttm = startDttm;
        this.restType = restType;
        this.lat = lat;
        this.lon = lon;
    }

}