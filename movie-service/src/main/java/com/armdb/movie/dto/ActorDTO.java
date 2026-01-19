package com.armdb.movie.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActorDTO {
    private java.util.UUID id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private String primaryProfession;
    private String knownForTitles;
    private Short age;
    private Boolean isAlive;
    
    public ActorDTO(java.util.UUID id, String name, Integer birthYear, Integer deathYear, String primaryProfession, String knownForTitles, Short age, Boolean isAlive) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
        this.age = age;
        this.isAlive = isAlive;
    }
}
