package com.armdb.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "casts")
@Data
@NoArgsConstructor
public class Cast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private java.util.UUID id;

    @Column(nullable = false, length = 15)
    private String nconst;

    @Column(name = "primary_name", nullable = false, length = 800)
    private String name;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "death_year")
    private Integer deathYear;

    @Column(name = "primary_profession", length = 800)
    private String primaryProfession;

    @Column(name = "known_for_titles", length = 800)
    private String knownForTitles;

    private Short age;

    @Column(name = "is_alive")
    private Boolean isAlive;
    
    public java.util.UUID getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
