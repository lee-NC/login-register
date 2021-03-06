package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Email.EmailService;
import com.jasu.loginregister.Entity.TutorStudent;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Repository.TutorStudentRepository;
import com.jasu.loginregister.Service.TutorStudentSerivce;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.CLASS_BEGINNING_CONTENT;
import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.CLASS_BEGINNING_SUBJECT;

@Service
@Slf4j
public class TutorStudentSerivceImpl implements TutorStudentSerivce {

    @Autowired
    private TutorStudentRepository tutorStudentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean createListStudentService(Long classId, Long tutorId, List<Long> studentIds) {
        if (studentIds.isEmpty()){
            return false;
        }
        try {
            for (Long studentId: studentIds){
                TutorStudent tutorStudent = UserMapper.toTutorStudent(classId,tutorId,studentId);
                User userBeApproved = userService.findByID(studentId);
                emailService.sendAnEmail(userBeApproved.getEmail(),CLASS_BEGINNING_CONTENT,CLASS_BEGINNING_SUBJECT);
                tutorStudentRepository.save(tutorStudent);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

}
