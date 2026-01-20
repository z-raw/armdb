package com.armdb.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "principals_v4")
@Data
@NoArgsConstructor
public class TitleCast {

    @EmbeddedId
    private TitleCastId id;

    @Column(length = 20)
    private String tconst;

    @Column(length = 20)
    private String nconst;

    @Column(length = 40)
    private String characters;

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
        return characters;
    }
}
