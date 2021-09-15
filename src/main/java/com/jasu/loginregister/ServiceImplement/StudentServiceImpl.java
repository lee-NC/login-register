package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Student;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateStudentRequest;
import com.jasu.loginregister.Repository.StudentRepository;
import com.jasu.loginregister.Service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student createStudent(CreateStudentRequest createStudentRequest) {
        log.info("Registry Student in Service");
        Student checkStudent = studentRepository.findByUserStudentId(createStudentRequest.getUserId());
        if(checkStudent!=null){
            throw new DuplicateRecordException("This user was a student");
        }
        Student student = UserMapper.toStudent(createStudentRequest);
        Student result = new Student();
        try {
            result = studentRepository.saveAndFlush(student);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return result;
    }

    @Override
    public Student findByUserId(Long userId) {
        Student checkStudent = studentRepository.findByUserStudentId(userId);
        if(checkStudent==null){
            throw new NotFoundException("No student found");
        }
        return checkStudent;
    }

    @Override
    public List<Student> getByListUserId(List<Long> userIds) {
        List<Student>students = new ArrayList<>();
        for (Long userId: userIds){
            Student student = studentRepository.findByUserStudentId(userId);
            if (student!=null){
                students.add(student);
            }
        }
        return students;
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.saveAndFlush(student);
    }
}
