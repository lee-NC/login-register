package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent,Long> {
    List<ClassStudent> findAllByUserCsIdAndState(Long userCreatedId, String state);
    ClassStudent findByClassroomCsIdAndUserCsId(Long classId, Long userId);
    List<ClassStudent> findAllByClassroomCsIdAndState(Long classId, String state);
    boolean existsByClassroomCsIdAndUserCsId(Long classId, Long userId);
}
