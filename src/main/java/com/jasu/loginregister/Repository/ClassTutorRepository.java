package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.ClassStudent;
import com.jasu.loginregister.Entity.ClassTutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassTutorRepository extends JpaRepository<ClassTutor,Long> {
    List<ClassTutor> findAllByUserCtIdAndState(Long tutorId, String state);
    ClassTutor findByClassroomCtIdAndUserCtId(Long classId,Long userId);
    List<ClassTutor> findByClassroomCtIdAndState(Long classId, String state);
    boolean existsByClassroomCtIdAndUserCtId(Long classId, Long userId);
}
