package com.jasu.loginregister.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jasu_tutor")
@Data
public class Tutor extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id",updatable = false)
    private User user;

    @Column(nullable = false)
    private String literacy;

    @Column(nullable = false)
    private String school;

    @Column()
    private String experience;

    @Column()
    private float assessment;

    @Column()
    private int numAssessment;

    @Column()
    private String achievement;

    public Tutor(Long id) {
        super(id);
    }

    public Tutor() {
        super();
    }

}
