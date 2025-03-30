package com.tmi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "word")
@Getter
@Setter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "word", nullable = false, length = 255)
    private String word;

    @Column(name = "meaning", length = 255)
    private String meaning;

    @Column(name = "fre_rank", nullable = true)
    private Long freRank;

    @Column(name = "pron", nullable = true, length = 255)
    private String pron;

    @Column(name = "pos", nullable = true, length = 255)
    private String pos;

    @Column(name = "category", nullable = true, length = 255)
    private String category;

    @Column(name = "tags", nullable = true, length = 255)
    private String tags;

    @Column(name = "word_level", nullable = true, length = 255)
    private String wordLevel;

    @Column(name = "create_dttm", nullable = true)
    private LocalDateTime createDttm;

    @Column(name = "update_dttm", nullable = true)
    private LocalDateTime updateDttm;

    @Column(name = "delete_dttm", nullable = true)
    private LocalDateTime deleteDttm;
}
