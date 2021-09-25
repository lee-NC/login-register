package com.jasu.loginregister.Repository;

import com.jasu.loginregister.Entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    List<Classroom> findAllByState(String state);
//    @Query(value = "SELECT * FROM jasu_classroom WHERE " +
//            "MATCH (subject, type, note) AGAINST (?1 IN NATURAL LANGUAGE MODE) " +
//            "AND state='WAITING' AND user_teach_id IS NULL",
//            countQuery = "SELECT count(*) FROM jasu_classroom",
//            nativeQuery = true)
    @Query(value = "SELECT c FROM Classroom c WHERE c.state='WAITING'" +
            "AND lower(CONCAT(c.subject, ' ', c.type, ' ', c.beginDay, ' ', c.numLesson, ' ',c.maxNum, ' ',c.fee, ' ',c.grade,' ',c.userTeachId))  " +
            " LIKE %?1%",
            countQuery = "SELECT count(c) FROM Classroom c")
    List<Classroom> searchClassRelative(String keyword);

    @Query(value = "SELECT * FROM jasu_classroom WHERE " +
            "MATCH (subject, type, note) AGAINST (?1 IN NATURAL LANGUAGE MODE) " +
            "AND state='WAITING'",
            countQuery = "SELECT count(*) FROM jasu_classroom",
            nativeQuery = true)
    List<Classroom> searchClassFullText(String keyword);
}
