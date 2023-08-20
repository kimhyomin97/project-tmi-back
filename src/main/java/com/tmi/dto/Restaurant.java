package com.tmi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Column(name = "address")
    private String address;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "license_dttm")
    private String licenseDttm;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "name")
    private String name;

    @Column(name = "rest_type")
    private String restType;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "tm_x")
    private Double tmX;

    @Column(name = "tm_y")
    private Double tmY;

    @Column(name = "update_dttm")
    private LocalDateTime updateDttm;

    public Restaurant(){

    }
    public Restaurant(LocalDateTime createDttm, String address, Double lat, String licenseDttm, Double lon, String name, String restType, String state, String postalCode, String homepage, Double tmX, Double tmY, LocalDateTime updateDttm) {
        this.createDttm = createDttm;
        this.address = address;
        this.lat = lat;
        this.licenseDttm = licenseDttm;
        this.lon = lon;
        this.name = name;
        this.restType = restType;
        this.state = state;
        this.postalCode = postalCode;
        this.homepage = homepage;
        this.tmX = tmX;
        this.tmY = tmY;
        this.updateDttm = updateDttm;
    }

}