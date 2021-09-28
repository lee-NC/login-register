package com.jasu.loginregister.Entity;


import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jasu_tutor")
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "tutor",orphanRemoval = true)
    private List<Achievement> achievements;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "tutor",orphanRemoval = true)
    private List<School> schools;


    public Tutor(Long id) {
        super(id);
    }
    public Tutor() {

    }

}
