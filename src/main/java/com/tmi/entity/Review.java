package com.tmi.entity;

import com.tmi.dto.Restaurant;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createDttm;
    private LocalDateTime updateDttm;
    private LocalDateTime deleteDttm;

    public void deleteReview() {
        this.deleteDttm = LocalDateTime.now();
    }

}
