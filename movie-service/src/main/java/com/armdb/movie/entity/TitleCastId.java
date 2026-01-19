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
    private java.util.UUID titleId;

    @Column(name = "cast_id")
    private java.util.UUID castId;

    public TitleCastId(java.util.UUID titleId, java.util.UUID castId) {
        this.titleId = titleId;
        this.castId = castId;
    }
}
