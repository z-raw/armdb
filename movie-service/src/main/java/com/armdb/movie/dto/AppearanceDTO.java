package com.armdb.movie.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppearanceDTO {
    private Integer movie_id;
    private String movie_name;
    private String character_name;
    
    public AppearanceDTO(Integer movie_id, String movie_name, String character_name) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.character_name = character_name;
    }
}
