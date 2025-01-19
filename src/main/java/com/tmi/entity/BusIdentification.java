package com.tmi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class BusIdentification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_nm", length = 100, nullable = false)
    private String busName;

    @Column(name = "bus_id", length = 100, nullable = false)
    private String busId;

    @Column(name = "reg_dttm", nullable = false)
    private LocalDateTime regDttm;

    @Column(name = "del_dttm")
    private LocalDateTime delDttm;

}
