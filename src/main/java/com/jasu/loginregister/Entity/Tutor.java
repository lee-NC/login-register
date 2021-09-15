package com.jasu.loginregister.Entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "jasu_tutor")
@Data

public class Tutor extends BaseEntity {

    @Column(nullable = false)
    private Long userTutorId;

    @Column(nullable = false)
    private String literacy;

    private float experience;

    @Column(nullable = false)
    private float assessment;

    @Column(nullable = false)
    private int numAssessment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "tutor")
    private List<Achievement> achievements;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "tutor")
    private List<School> schools;


    public Tutor(Long id) {
        super(id);
    }
    public Tutor() {

    }

}
