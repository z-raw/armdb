package com.armdb.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TitleCastId implements Serializable {

    @Column(name = "title_id")
    private Integer titleId;

    @Column(name = "cast_id")
    private Integer castId;

    public TitleCastId(Integer titleId, Integer castId) {
        this.titleId = titleId;
        this.castId = castId;
    }
}
