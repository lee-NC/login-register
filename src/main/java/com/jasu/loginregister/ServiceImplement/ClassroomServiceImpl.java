package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Exception.InternalServerException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.ClassDto;
import com.jasu.loginregister.Model.Mapper.ClassMapper;
import com.jasu.loginregister.Model.Request.RelatedToClass.CreateClassroomRequest;
import com.jasu.loginregister.Repository.ClassroomRepository;
import com.jasu.loginregister.Service.ClassroomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public ClassDto createClassroom(CreateClassroomRequest createClassroomRequest,String roleKKey, Long userCreateId) {
        log.info("Create Classroom in Service");
        Classroom classroom = ClassMapper.toClass(createClassroomRequest,roleKKey,userCreateId);
        long dayToStart = checkTime(classroom.getBeginDay());
        if (Math.abs(dayToStart)>14){//begin day smaller 2 weeks
            throw new InternalServerException("Begin day is too far");
        }
        Classroom classroomSaved = new Classroom();
        try{
            classroomSaved = classroomRepository.saveAndFlush(classroom);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ClassMapper.toClassDto(classroomSaved);
    }


    @Override
    public List<ClassDto> getListClass(List<Long> classIds) {
        List<ClassDto> classDtos = new ArrayList<>();
        try {
            for (Long id:classIds){
                classDtos.add(ClassMapper.toClassDto(classroomRepository.getById(id)));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return classDtos;
    }

    @Override
    public Classroom findById(Long classId) {
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        if (!classroom.isPresent()){
            throw new NotFoundException("No class found");
        }
        return classroom.get();
    }


    @Override
    public Classroom updateClassroom(Classroom classroom) {
        Classroom saveClass = new Classroom();
        try {
            saveClass = classroomRepository.saveAndFlush(classroom);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return saveClass;
    }


    //check time between two days
    private long checkTime(String createTime){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String nowTime = formatter.format(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate lastDate = LocalDate.parse(createTime, dateTimeFormatter);
        LocalDate nowDate = LocalDate.parse(nowTime, dateTimeFormatter);
        if (!lastDate.isAfter(nowDate)){
            throw new InternalServerException("Begin day is invalid");
        }
        long daysBetween = ChronoUnit.DAYS.between(lastDate, nowDate);
        return daysBetween;
    }

}
