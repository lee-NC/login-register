package com.jasu.loginregister.Entity;

import com.jasu.loginregister.Entity.DefinitionEntity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jasu_classroom")
@Data
public class Classroom  extends BaseEntity {


    private Long userTeachId;

    @Column(nullable = false)
    private int maxNum;

    @Column(nullable = false,length = 10)
    private String type;

    @Column(nullable = false)
    private int currentNum;

    @Column(nullable = false,updatable = false)
    private String subject;

    @Column(nullable = false,updatable = false)
    private int grade;

    @Column(length = 150)
    private String note;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "jasu_schedule",joinColumns = {@JoinColumn(name = "classroom_schedule_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lesson_schedule_id",referencedColumnName = "id")})
    private List<Lesson> lesson;

    @Column(nullable = false)
    private Long fee;

    @Column(nullable = false)
    private int numLesson;

    @Column(nullable = false)
    private String beginDay;

    @Column(nullable = false,updatable = false)
    private String state;

    public Classroom() {
    }

}
