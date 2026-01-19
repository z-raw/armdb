package com.armdb.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "titles_v2")
@Data
@NoArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private java.util.UUID id;

    @Column(unique = true, length = 20)
    private String tconst;

    @Column(name = "primary_title", length = 800)
    private String primaryTitle;

    @Column(name = "is_adult")
    private Boolean isAdult;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(length = 800)
    private String genres;

    public java.util.UUID getId() {
        return id;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }
    
    public Integer getStartYear() {
        return startYear;
    }
}
