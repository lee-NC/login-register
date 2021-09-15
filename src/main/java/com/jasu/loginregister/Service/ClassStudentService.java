package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.ClassStudent;

import java.util.List;

public interface ClassStudentService {
    ClassStudent createClassroomStudent(Long userId, Long id,String state);

    List<ClassStudent> getListClassStudentByUserIdAndState(Long userCreatedId,String state);

    Boolean checkRecentClassStudent(Long userId, String state);

    Boolean updateClassroomStudent(ClassStudent classStudent);

    List<Long> getListUserID(Long classId, String state);

    Boolean rejectStudentInAClassroom(Long classId, String reject);

    ClassStudent findByClassIdAndUserId(Long classId, Long userApprovedId);
}
