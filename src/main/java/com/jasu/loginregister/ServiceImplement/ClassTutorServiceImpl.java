package com.jasu.loginregister.ServiceImplement;


import com.jasu.loginregister.Entity.ClassStudent;
import com.jasu.loginregister.Entity.ClassTutor;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Repository.ClassTutorRepository;
import com.jasu.loginregister.Service.ClassTutorService;
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
public class ClassTutorServiceImpl implements ClassTutorService {

    @Autowired
    private ClassTutorRepository classTutorRepository;

    @Override
    public ClassTutor createClassroomTutor(Long tutorId, Long classId,String state) {
        log.info("Create class tutor in Service ");
        ClassTutor classTutor = ClassMapper.toClassTutor(tutorId, classId, state);
        ClassTutor result = null;
        try{
            result =  classTutorRepository.saveAndFlush(classTutor);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<Long> getListClassTutorByUserIdAndState(Long userCreatedId,String state) {

        log.info("Get List class ");
        List<ClassTutor> classTutors = classTutorRepository.findAllByUserCtIdAndState(userCreatedId,state);
        List<Long> classTutorId = new ArrayList<Long>();
        for (ClassTutor classTutor:classTutors) {
            classTutorId.add(classTutor.getClassroomCtId());
        }
        return classTutorId;
    }

    @Override
    public Boolean checkRecentClassTutor(Long userId, String state) {
        log.info("Check recent class in Service");

        //get Now day
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String []nowDay = formatter.format(date).split("/");

        //get list recent class
        List<ClassTutor> classTutors = classTutorRepository.findAllByUserCtIdAndState(userId,state);

        //set rank for each state
        int limitRank = 0;
        if (state.equals("CREATE")) limitRank = 3;
        if (state.equals("SIGNUP")) limitRank = 5;

        if (classTutors.size()>limitRank) {
            classTutors = classTutors.subList(classTutors.size()-limitRank,classTutors.size());
        }

        for (ClassTutor classTutor:classTutors) {
            String []day = classTutor.getCreatedAt().split("/");
            if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   {
                limitRank--;
            }
        }

        if (limitRank<=0) return false;
        return true;
    }

    @Override
    public boolean updateClassroomTutor(ClassTutor classTutor) {
        log.info("Update ClassTutor in Service");
        ClassTutor result = classTutorRepository.saveAndFlush(classTutor);
        if(result!=null) return true;
        return false;
    }

    @Override
    public List<Long> getListUserID(Long classId, String state) {
        List<ClassTutor> listClassStudent = classTutorRepository.findByClassroomCtIdAndState(classId,state);
        List<Long> listUserId = new ArrayList<>();
        for(ClassTutor classTutor: listClassStudent){
            listUserId.add(classTutor.getUserCtId());
        }
        return listUserId;
    }


    @Override
    public boolean rejectAllClassroomTutor(Long classId, String state) {
        log.info("Update ClassTutor in Service");
        List<ClassTutor> classTutors = classTutorRepository.findByClassroomCtIdAndState(classId,state);
        if (classTutors==null)    return false;
        for (ClassTutor classTutor:classTutors) {
            classTutor.setState(STATE_REJECTED);
            classTutorRepository.saveAndFlush(classTutor);
        }
        return true;
    }

    @Override
    public ClassTutor findByClassIdAndUserId(Long classId, Long userApprovedId) {
        log.info("Find ClassTutor in Service");
        ClassTutor classTutor = classTutorRepository.findByClassroomCtIdAndUserCtId(classId,userApprovedId);
        if (classTutor.equals(null)){
            throw new NotFoundException("No class tutor found");
        }
        return classTutor;
    }

    @Override
    public boolean existByClassIdAndUserId(Long classId, Long userId) {
        log.info("Check exist in Service");
        return classTutorRepository.existsByClassroomCtIdAndUserCtId(classId,userId);
    }


}
