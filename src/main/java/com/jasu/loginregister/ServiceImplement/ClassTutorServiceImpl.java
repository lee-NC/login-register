package com.jasu.loginregister.ServiceImplement;


import com.jasu.loginregister.Entity.ClassTutor;
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

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

@Service
@Slf4j
@Transactional
public class ClassTutorServiceImpl implements ClassTutorService {

    @Autowired
    private ClassTutorRepository classTutorRepository;

    @Override
    public void createClassroomTutor(Long tutorId, Long classId,String state) {
        log.info("Create class tutor in Service ");
        try {
            ClassTutor classTutor = ClassMapper.toClassTutor(tutorId, classId, state);
            classTutorRepository.saveAndFlush(classTutor);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ClassTutor> getListClassTutorByUserIdAndState(Long userCreatedId,String state) {

        log.info("Get List class in service ");
        List<ClassTutor> classTutors = classTutorRepository.findAllByUserCtIdAndState(userCreatedId,state);
        if (classTutors.isEmpty()){
            throw new NotFoundException("No class tutor found");
        }
        return classTutors;
    }

    @Override
    public Boolean checkRecentClassTutor(Long userId, String state) {
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
            List<ClassTutor> classTutors = classTutorRepository.findAllByUserCtIdAndState(userId,state);
            if (classTutors.isEmpty()){
                return true;
            }

            if (classTutors.size()>limitRank) {
                classTutors = classTutors.subList(classTutors.size()-limitRank,classTutors.size());
            }

            for (ClassTutor classTutor:classTutors) {
                String []day = classTutor.getCreatedAt().split("/");
                if (Integer.parseInt(day[1])==Integer.parseInt((nowDay[0])))   {
                    limitRank--;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (limitRank<=0) return false;
        return true;
    }

    @Override
    public boolean updateClassroomTutor(ClassTutor classTutor) {
        log.info("Update ClassTutor in Service");
        try {
            ClassTutor result = classTutorRepository.saveAndFlush(classTutor);
            if(result!=null) return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Long> getListUserID(Long classId, String state) {
        List<Long> listUserId = new ArrayList<>();
        try {
            List<ClassTutor> listClassStudent = classTutorRepository.findByClassroomCtIdAndState(classId,state);

            for(ClassTutor classTutor: listClassStudent){
                listUserId.add(classTutor.getUserCtId());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return listUserId;
    }


    @Override
    public boolean updateListTutorClassroomTutor(Long classId,String beforeState, String afterState) {
        log.info("Update ClassTutor in Service");
        List<ClassTutor> classTutors = classTutorRepository.findByClassroomCtIdAndState(classId,beforeState);
        if (classTutors==null)    return false;
        for (ClassTutor classTutor:classTutors) {
            classTutor.setState(afterState);
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
