package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "jasu_class_tutor")
@NoArgsConstructor
public class ClassTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id",referencedColumnName = "id",updatable = false)
    private Classroom classroom;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",referencedColumnName = "id",updatable = false)
    private Tutor tutor;



    private String state;

    private Boolean likeClass;

    public ClassTutor(Long id) {
        this.id = id;
    }
}
