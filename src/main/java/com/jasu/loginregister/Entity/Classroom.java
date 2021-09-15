package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "jasu_classroom")
@Data
public class Classroom  extends BaseEntity{


    private Long userTeachId;

    @Column(nullable = false)
    private int maxNum;

    @Column(nullable = false,length = 10)
    private String type;

    @Column(nullable = false)
    private int currentNum;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private int grade;

    @Column()
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

    @Column(nullable = false)
    private String state;

    public Classroom() {
    }

}
