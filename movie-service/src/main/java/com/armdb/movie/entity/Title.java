package com.armdb.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "download_date_id")
    private Integer downloadDateId;

    @Column(unique = true, nullable = false, length = 20)
    private String tconst;

    @Column(length = 40)
    private String type;

    @Column(name = "primary_title", nullable = false, length = 800)
    private String primaryTitle;

    @Column(name = "original_title", length = 800)
    private String originalTitle;

    @Column(name = "is_adult")
    private Boolean isAdult;

    @Column(name = "start_year")
    private Short startYear;

    @Column(name = "end_year")
    private Short endYear;

    @Column(name = "runtime_minutes")
    private Short runtimeMinutes;

    @Column(name = "genre_1", length = 25)
    private String genre1;

    @Column(name = "genre_2", length = 25)
    private String genre2;

    @Column(name = "genre_3", length = 25)
    private String genre3;

    public Integer getId() {
        return id;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }
    
    public Short getStartYear() {
        return startYear;
    }
}
