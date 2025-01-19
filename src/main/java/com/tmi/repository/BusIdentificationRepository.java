package com.tmi.repository;

import com.tmi.entity.BusIdentification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusIdentificationRepository extends JpaRepository<BusIdentification, Long> {
    boolean existsByBusIdAndDelDttmIsNull(String busId);
}
