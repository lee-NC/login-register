package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.ClassStudent;

import java.util.List;

public interface ClassStudentService {
    void createClassroomStudent(Long userId, Long id,String state);

    List<ClassStudent> getListClassStudentByUserIdAndState(Long userCreatedId,String state);

    Boolean checkRecentClassStudent(Long userId, String state);

    Boolean updateClassroomStudent(ClassStudent classStudent);

    List<Long> getListUserID(Long classId, String state);

    Boolean rejectStudentInClassroom(Long classId);

    ClassStudent findByClassIdAndUserId(Long classId, Long userApprovedId);

    Boolean existByClassIdAndUserId(Long classId, Long userApplyId);
}
