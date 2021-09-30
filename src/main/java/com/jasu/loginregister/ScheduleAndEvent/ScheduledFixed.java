package com.jasu.loginregister.ScheduleAndEvent;

import com.jasu.loginregister.Email.*;
import com.jasu.loginregister.Repository.*;
import com.jasu.loginregister.Service.*;
import com.jasu.loginregister.Entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.*;

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

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedDelay = 180000)
    public void updateStateClass() {
        log.info("Test refresh token up to time- The time is now {}", formatter.format(new Date()));
        refreshTokenService.updateByDelete();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkClassBeginTime() {
        log.info("Check Classroom " + new Date());

        List<Classroom> classroomList = classroomRepository.findAllByState(STATE_WAITING);
        if (classroomList.isEmpty()){
            return;
        }
        for (Classroom classroom:classroomList) {
            long dayToStart = checkTime(classroom.getBeginDay());
            if (dayToStart<=0){//den ngay hoan thanh
                User userCreate = userService.findByID(Long.parseLong(classroom.getCreatedBy()));
                if (classroom.getUserTeachId()!=null){


                    Long fee = ((classroom.getFee()/20)*classroom.getMaxNum())/100;
                    ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classroom.getId(),Long.parseLong(classroom.getCreatedBy()));


                    if (classroom.getCurrentNum()>0){
                        classTutor.setState(STATE_PROCESSING);

                        classroom.setState(STATE_PROCESSING);
                        classroomRepository.saveAndFlush(classroom);
                        if (classTutorService.updateClassroomTutor(classTutor)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPLY, STATE_REJECTED)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPROVED,STATE_PROCESSING)){

                            List <Long> studentIds = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_PROCESSING);
                            List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_REJECTED);
                            if (tutorStudentSerivce.createListStudentService(classroom.getId(),Long.parseLong(classroom.getCreatedBy()),studentIds)
                                    &&userService.refundUserBeRejected(studentBeRejectedId,fee)) {
                                emailService.sendAnEmail(userCreate.getEmail(),CLASS_BEGINNING_CONTENT,CLASS_BEGINNING_SUBJECT);
                                continue;
                            }
                        }
                    }
                    else {
                        classroom.setState(STATE_CANCELED);
                        classroomRepository.saveAndFlush(classroom);

                        classTutor.setState(STATE_CANCELED);
                        if (classTutorService.updateClassroomTutor(classTutor)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPLY, STATE_REJECTED)
                                &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_APPROVED,STATE_REJECTED)){
                            List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_REJECTED);
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
                            &&classTutorService.updateListTutorClassroomTutor(classroom.getId(),STATE_APPLY, STATE_REJECTED)){

                        //lay 12.5% tien phi 1 buoi hoc chia cho so nguoi tham gia cho 1 lan dang ki
                        Long fee = ((classroom.getFee()/8)/classroom.getMaxNum())/100;
                        List<Long> studentBeRejectedId = classStudentService.getListUserIDByClassIdAndState(classroom.getId(),STATE_REJECTED);
                        if (userService.refundUserBeRejected(studentBeRejectedId,fee)) {
                            emailService.sendAnEmail(userCreate.getEmail(),CLASS_CREATE_CANCEL_CONTENT, CLASS_CREATE_CANCEL_SUBJECT);
                            continue;
                        }
                    }

                }
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * *")
    public void checkClassDoneTime() {
        log.info("Check Classroom " + new Date());

        List<Classroom> classroomList = classroomRepository.findAllByState(STATE_PROCESSING);
        if (classroomList.isEmpty()){
            return;
        }
        for (Classroom classroom:classroomList) {
            long dayToStart = checkTime(endTime(classroom));
            if (dayToStart<=0){//den ngay hoan thanh
                ClassTutor classTutor = classTutorService.findByClassIdAndUserId(classroom.getId(),Long.parseLong(classroom.getCreatedBy()));
                classTutor.setState(STATE_DONE);
                classroom.setState(STATE_DONE);
                classroomRepository.saveAndFlush(classroom);
                if (classTutorService.updateClassroomTutor(classTutor)
                        &&classStudentService.updateListStudentInClassroom(classroom.getId(),STATE_PROCESSING, STATE_DONE)){
                    continue;
                }
            }
        }
    }

//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        log.info("Rate - The time is now {}", formatter.format(new Date()));
//    }

    //check time between two days
    private long checkTime(String checkTime){
        Date date = new Date();
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        String nowTime = formatterDate.format(date);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate lastDate = LocalDate.parse(checkTime, dateTimeFormatter);
        LocalDate nowDate = LocalDate.parse(nowTime, dateTimeFormatter);
        return ChronoUnit.DAYS.between(lastDate, nowDate);
    }

    private String endTime(Classroom classroom){
        int count = classroom.getLesson().size();
        int numWeeks = classroom.getNumLesson()/count;
        int surplus = classroom.getNumLesson()%count;

        Date lastDate = null;
        try {
            lastDate = new SimpleDateFormat("dd/MM/yyyy").parse(classroom.getBeginDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDate);
        calendar.add(Calendar.DATE, numWeeks*7+surplus);
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        return formatterDate.format(calendar.getTime());
    }
}