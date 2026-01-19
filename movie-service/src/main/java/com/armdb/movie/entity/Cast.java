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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String name;

    private Short age;

    @Column(name = "is_alive")
    private Boolean isAlive;

    @Column(nullable = false, length = 15)
    private String nconst;
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
