package com.jasu.loginregister.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jasu_classroom")
@Data
public class Classroom  extends BaseEntity{

    private Long tutorTeachId;

    private int maxNum;

    private String type;

    private int currentNum;

    private String subject;

    private int grade;

    private String schedule;

    private Date beginDay;

    private Date endDay;

    private String state;

    public Classroom(Long id) {
        super(id);
    }

    public Classroom() {
    }

}
