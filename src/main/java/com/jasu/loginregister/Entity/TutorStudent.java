package com.jasu.loginregister.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "jasu_tutor_student")
@NoArgsConstructor
public class TutorStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long classTsId;

    @Column(nullable = false)
    private Long userTutorTsId;

    @Column(nullable = false)
    private Long userStudentTsId;

    @Column(nullable = false)
    private int studentAssessment;

    @Column(nullable = false)
    private int tutorAssessment;

    @Column(nullable = false)
    private Boolean studentLike;

    @Column(nullable = false)
    private Boolean tutorLike;

    @Column(nullable = false)
    private Boolean isInvite;

    @Column()
    private Long classInviteId;

    @Column(nullable = false)
    private Boolean isCancel;

}
