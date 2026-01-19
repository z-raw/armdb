package com.armdb.movie.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActorDTO {
    private Integer id;
    private String name;
    
    public ActorDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
