package com.jasu.loginregister.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "jasu_tutor_student")
public class TutorStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int studentAssessment;

    private int tutorAssessment;

    private Boolean studentLike;

    private Boolean tutorLike;


}
