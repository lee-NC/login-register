package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Request.RelatedToClass.CreateClassroomRequest;

import java.util.List;

public interface ClassroomService {
    ClassDto createClassroom(CreateClassroomRequest createClassroomRequest,String roleKey, Long userCreateId);

    List<ClassDto> getListClass(List<Long> classIds);

    Classroom findById(Long classId);

    Classroom updateClassroom(Classroom classroom);

}
