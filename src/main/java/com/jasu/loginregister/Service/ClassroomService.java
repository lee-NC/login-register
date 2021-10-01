package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Request.RelatedToClass.CreateClassroomRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomService {
    ClassDto createClassroom(CreateClassroomRequest createClassroomRequest,String roleKey, Long userCreateId);

    List<ClassDto> getListClass(List<Long> classIds);

    Classroom findById(Long classId);

    void updateClassroom(Classroom classroom);

    List<ClassDto> searchClass(String keyWord);

    Page<Classroom> listAll(int pageNum, String sortField, String sortDir);

    List<ClassDto> suggestClass(User user,int grade);
}
