package com.armdb.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "titles_casts")
@Data
@NoArgsConstructor
public class TitleCast {

    @EmbeddedId
    private TitleCastId id;

    @Column(length = 300)
    private String character;

    @ManyToOne
    @MapsId("titleId")
    @JoinColumn(name = "title_id")
    private Title title;

    @ManyToOne
    @MapsId("castId")
    @JoinColumn(name = "cast_id")
    private Cast cast;
    
    public Title getTitle() {
        return title;
    }
    
    public String getCharacter() {
        return character;
    }
}
