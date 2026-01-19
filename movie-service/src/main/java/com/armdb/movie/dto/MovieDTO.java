package com.armdb.movie.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieDTO {
    private java.util.UUID id;
    private String title;
    private String year;
    
    public MovieDTO(java.util.UUID id, String title, Integer startYear) {
        this.id = id;
        this.title = title;
        this.year = startYear != null ? String.valueOf(startYear) : null;
    }
}
