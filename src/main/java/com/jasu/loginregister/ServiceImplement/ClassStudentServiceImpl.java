package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.ClassStudent;
import com.jasu.loginregister.Entity.ClassTutor;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Repository.ClassStudentRepository;
import com.jasu.loginregister.Service.ClassStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.*;

@Service
@Slf4j
@Transactional
public class ClassStudentServiceImpl implements ClassStudentService {

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Override
    public void createClassroomStudent(Long userId, Long classId,String state) {
        log.info("Create ClassStudent in Service");
        try {
            ClassStudent classStudent = ClassMapper.toClassStudent(userId,classId,state);
            classStudentRepository.saveAndFlush(classStudent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ClassStudent> getListClassStudentByUserIdAndState(Long userCreatedId,String state) {
        log.info("Get ClassStudent in Service");
        List<ClassStudent> classStudents = classStudentRepository.findAllByUserCsIdAndState(userCreatedId,state);
        if (classStudents.isEmpty()){
            throw new NotFoundException("No class student found");
        }
        return classStudents;
    }

    @Override
    public Boolean checkRecentClassStudent(Long userId, String state) {
        log.info("Check recent class in Service");

        //get Now day
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        //set rank for each state
        int limitRank = 0;
        if (state.equals(STATE_CREATE)) limitRank = 3;
        if (state.equals(STATE_APPLY)) limitRank = 5;
        String []nowDay = formatter.format(date).split("/");

        try {
            //get list recent class
            List<ClassStudent> classStudents= classStudentRepository.findAllByUserCsIdAndState(userId,state);
            if (classStudents.isEmpty()){
                return true;
            }

            if (classStudents.size()>limitRank) {
                classStudents = classStudents.subList(classStudents.size()-limitRank,classStudents.size());
            }

            for (ClassStudent classStudent:classStudents) {
                String []day = classStudent.getCreatedAt().split("/");
                if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   {
                    limitRank--;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (limitRank <= 0) return false;
        return true;
    }

    @Override
    public Boolean updateClassroomStudent(ClassStudent classStudent) {
        log.info("Update ClassStudent in Service");
        try {
            ClassStudent result = classStudentRepository.saveAndFlush(classStudent);
            if(result!=null) return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Long> getListUserID(Long classId, String state) {
        log.info("get List UserID in Service");
        List<Long> listUserId = new ArrayList<>();
        try {
            List<ClassStudent> listClassStudent = classStudentRepository.findAllByClassroomCsIdAndState(classId,state);

            for(ClassStudent classStudent: listClassStudent){
                listUserId.add(classStudent.getUserCsId());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return listUserId;
    }

    @Override
    public Boolean rejectStudentInClassroom(Long classId) {
        log.info("Update ClassStudent in Service");
        try {
            List<ClassStudent> classStudents = classStudentRepository.findAllByUserCsIdAndState(classId,STATE_APPLY);
            for (ClassStudent classStudent:classStudents) {
                classStudent.setState(STATE_REJECTED);
                classStudentRepository.saveAndFlush(classStudent);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public ClassStudent findByClassIdAndUserId(Long classId, Long userId) {
        log.info("Update ClassStudent in Service");
        ClassStudent classStudent = classStudentRepository.findByClassroomCsIdAndUserCsId(classId,userId);
        if (classStudent == null){
            throw new NotFoundException("No class student found");
        }
        return classStudent;
    }

    @Override
    public Boolean existByClassIdAndUserId(Long classId, Long userId) {
        log.info("Check exist in Service");
        return classStudentRepository.existsByClassroomCsIdAndUserCsId(classId,userId);
    }


}
