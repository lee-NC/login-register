package com.jasu.loginregister.ScheduleAndTimer;

import com.jasu.loginregister.Entity.ClassStudent;
import com.jasu.loginregister.Entity.ClassTutor;
import com.jasu.loginregister.Entity.Classroom;
import com.jasu.loginregister.Entity.RefreshToken;
import com.jasu.loginregister.Repository.*;
import com.jasu.loginregister.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;
import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.STATE_CANCELED;

@EnableAsync
@Component
@Slf4j
public class ScheduledFixed{

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassStudentService classStudentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClassTutorService classTutorService;

    @Autowired
    private TutorStudentSerivce tutorStudentSerivce;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Scheduled(fixedDelay = 60000)
    public void updateStateClass() {
        log.info("Test refresh token up to time- The time is now {}", formatter.format(new Date()));
        refreshTokenService.updateByDelete();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void reportTime() {
        log.info("Check Classroom " + new Date());

        List<Classroom> classroomList = classroomRepository.findAllByState(STATE_WAITING);
        if (classroomList.isEmpty()){
            return;
        }
        for (Classroom classroom:classroomList) {
            long dayToStart = checkTime(classroom.getBeginDay());
            if (dayToStart<=0){//den ngay hoan thanh
                if (classroom.getUserTeachId()!=null){
                    if (classroom.getCurrentNum()>0){

                        classroom.setState(STATE_PROCESSING);
                        classroomRepository.saveAndFlush(classroom);

                        ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classroom.getId(),Long.parseLong(classroom.getCreatedBy()));
                        classTutor.setState(STATE_PROCESSING);
                        if (classTutorService.updateClassroomTutor(classTutor)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPLY, STATE_REJECTED)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPROVED,STATE_PROCESSING)){
                            Long fee = ((classroom.getFee()/20)*classroom.getMaxNum())/100;
                            List <Long> studentIds = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_PROCESSING);
                            List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_REJECTED);
                            if (tutorStudentSerivce.createListStudentService(classroom.getId(),Long.parseLong(classroom.getCreatedBy()),studentIds)
                                    &&userService.refundUserBeRejected(studentBeRejectedId,fee)) {
                                continue;
                            }
                        }
                    }
                    else {
                        classroom.setState(STATE_CANCELED);
                        classroomRepository.saveAndFlush(classroom);

                        ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classroom.getId(),Long.parseLong(classroom.getCreatedBy()));
                        classTutor.setState(STATE_CANCELED);

                        if (classTutorService.updateClassroomTutor(classTutor)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPLY, STATE_CANCELED)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPROVED,STATE_CANCELED)){
                            Long fee = ((classroom.getFee()/20)*classroom.getMaxNum())/100;
                            List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_CANCELED);
                            if (userService.refundUserBeRejected(studentBeRejectedId,fee)) {
                                continue;
                            }
                        }
                    }
                }
                else {
                    classroom.setState(STATE_CANCELED);
                    classroomRepository.saveAndFlush(classroom);

                    ClassStudent classStudent = classStudentService.findByClassIdAndUserId(classroom.getId(),Long.parseLong(classroom.getCreatedBy()));
                    classStudent.setState(STATE_CANCELED);

                    if (classStudentService.updateClassroomStudent(classStudent)
                            &&classTutorService.updateListTutorClassroomTutor(classroom.getId(),STATE_APPLY, STATE_CANCELED)){
                        //lay 12.5% tien phi 1 buoi hoc chia cho so nguoi tham gia cho 1 lan dang ki
                        Long fee = ((classroom.getFee()/8)/classroom.getMaxNum())/100;
                        List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_CANCELED);
                        if (userService.refundUserBeRejected(studentBeRejectedId,fee)) {
                            continue;
                        }
                    }

                }
            }
        }
    }
//
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        log.info("Rate - The time is now {}", formatter.format(new Date()));
//    }

    //check time between two days
    private long checkTime(String createTime){
        Date date = new Date();
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        String nowTime = formatterDate.format(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate lastDate = LocalDate.parse(createTime, dateTimeFormatter);
        LocalDate nowDate = LocalDate.parse(nowTime, dateTimeFormatter);
        return ChronoUnit.DAYS.between(lastDate, nowDate);
    }
}