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

import static com.jasu.loginregister.Entity.DefineEntityStateMessage.STATE_REJECTED;

@Service
@Slf4j
@Transactional
public class ClassStudentServiceImpl implements ClassStudentService {

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Override
    public ClassStudent createClassroomStudent(Long userId, Long classId,String state) {
        log.info("Create ClassStudent in Service");
        ClassStudent classStudent = ClassMapper.toClassStudent(userId,classId,state);
        ClassStudent result = null;
        try{
            classStudentRepository.saveAndFlush(classStudent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<Long> getListClassStudentByUserIdAndState(Long userCreatedId,String state) {
        log.info("Create ClassStudent in Service");
        List<ClassStudent> classStudents = classStudentRepository.findAllByUserCsIdAndState(userCreatedId,state);
        List<Long> classStudentId = new ArrayList<>();
        for (ClassStudent classStudent:classStudents) {
            classStudentId.add(classStudent.getClassroomCsId());
        }
        return classStudentId;
    }

    @Override
    public Boolean checkRecentClassStudent(Long userId, String state) {
        log.info("Check recent class in Service");

        //get Now day
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String []nowDay = formatter.format(date).split("/");

        //get list recent class
        List<ClassStudent> classStudents= classStudentRepository.findAllByUserCsIdAndState(userId,state);

        //set rank for each state
        int limitRank = 0;
        if (state.equals("CREATE")) limitRank = 3;
        if (state.equals("SIGNUP")) limitRank = 5;

        if (classStudents.size()>limitRank) {
            classStudents = classStudents.subList(classStudents.size()-limitRank,classStudents.size());
        }

        for (ClassStudent classStudent:classStudents) {
            String []day = classStudent.getCreatedAt().split("/");
            if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   {
                limitRank--;
            }
        }

        if (limitRank <= 0) return false;
        return true;
    }

    @Override
    public Boolean updateClassroomStudent(ClassStudent classStudent) {
        log.info("Update ClassStudent in Service");
        ClassStudent result = classStudentRepository.saveAndFlush(classStudent);
        if(result!=null) return true;
        return false;
    }

    @Override
    public List<Long> getListUserID(Long classId, String state) {
        List<ClassStudent> listClassStudent = classStudentRepository.findAllByClassroomCsIdAndState(classId,state);
        List<Long> listUserId = new ArrayList<>();
        for(ClassStudent classStudent: listClassStudent){
            listUserId.add(classStudent.getUserCsId());
        }
        return listUserId;
    }

    @Override
    public Boolean rejectStudentInAClassroom(Long classId, String state) {
        log.info("Update ClassStudent in Service");
        List<ClassStudent> classStudents = classStudentRepository.findAllByUserCsIdAndState(classId,state);
        if (classStudents==null)    return false;
        for (ClassStudent classStudent:classStudents) {
            classStudent.setState(STATE_REJECTED);
            classStudentRepository.saveAndFlush(classStudent);
        }
        return true;
    }

    @Override
    public ClassStudent findByClassIdAndUserId(Long classId, Long userId) {
        log.info("Update ClassStudent in Service");
        ClassStudent classStudent = classStudentRepository.findByClassroomCsIdAndUserCsId(classId,userId);
        if (classStudent.equals(null)){
            throw new NotFoundException("No class student found");
        }
        return classStudent;
    }


}
