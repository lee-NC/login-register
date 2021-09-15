package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    List<Classroom> findByCreatedByAndState(String userCreatedId, String state);
}
