package com.armdb.movie.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieDTO {
    private Integer id;
    private String title;
    private String year;
    
    public MovieDTO(Integer id, String title, Short startYear) {
        this.id = id;
        this.title = title;
        this.year = startYear != null ? String.valueOf(startYear) : null;
    }
}
