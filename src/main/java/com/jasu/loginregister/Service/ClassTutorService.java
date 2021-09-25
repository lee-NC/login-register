package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.ClassTutor;

import java.util.List;

public interface ClassTutorService {
    void createClassroomTutor(Long tutorId, Long classId, String state);

    List<ClassTutor> getListClassTutorByUserIdAndState(Long userCreatedId,String state);

    Boolean checkRecentClassTutor(Long userId, String state);

    boolean updateClassroomTutor(ClassTutor classTutor);

    List<Long> getListUserID(Long classId, String state);

    boolean updateListTutorClassroomTutor(Long classId,String beforeState, String afterState);

    ClassTutor findByClassIdAndUserId(Long classId, Long userApprovedId);

    boolean existByClassIdAndUserId(Long classId, Long userId);
}
