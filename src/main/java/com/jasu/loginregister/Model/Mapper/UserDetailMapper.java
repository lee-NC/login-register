package com.jasu.loginregister.Model.Mapper;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.InternalServerException;
import com.jasu.loginregister.Model.Dto.BasicDto.AchievementDto;
import com.jasu.loginregister.Model.Dto.BasicDto.AddressDto;
import com.jasu.loginregister.Model.Dto.BasicDto.SchoolDto;
import com.jasu.loginregister.Model.Dto.DetailDto.StudentDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.TutorDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.UserDetailDto;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateAddressRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserDetailMapper {

    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

    public static UserDetailDto toUserDetailDto(User user) {
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setId(user.getId());
        userDetailDto.setFullName(user.getFullName());
        userDetailDto.setAddressDto(toAddressDto(user.getAddress()));
        userDetailDto.setBirthday(user.getBirthday());
        userDetailDto.setAvatar(user.getAvatar());
        userDetailDto.setGender(user.getGender());
        return userDetailDto;
    }

    private static AddressDto toAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();

        addressDto.setAddressDetail(address.getAddressDetail());
        addressDto.setWard(address.getWard());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setProvince(address.getProvince());
        addressDto.setPhoneNumber(address.getPhoneNumber());

        return addressDto;
    }

    public static TutorDetailDto toTutorDetailDto(Tutor tutor) {
        TutorDetailDto tutorDetailDto = new TutorDetailDto();
        tutorDetailDto.setLiteracy(tutor.getLiteracy());
        tutorDetailDto.setListAchievement(toAchievementDto(tutor.getAchievements()));
        tutorDetailDto.setListSchool(toSchoolDto(tutor.getSchools()));
        tutorDetailDto.setExperience(tutor.getExperience());
        return tutorDetailDto;
    }

    public static Set<SchoolDto> toSchoolDto(List<School> schools) {
        Set<SchoolDto> schoolDtos = new HashSet<>();
        for (School school:schools) {
            SchoolDto schoolDto = new SchoolDto(school.getId(),school.getSchoolName());
            schoolDtos.add(schoolDto);
        }
        return schoolDtos;
    }

    public static Set<AchievementDto> toAchievementDto(List<Achievement> achievements) {
        Set<AchievementDto> achievementDtos = new HashSet<>();
        for (Achievement achievement:achievements) {
            AchievementDto achievementDto = new AchievementDto(achievement.getId(),achievement.getAchievement(),achievement.getYear());
            achievementDtos.add(achievementDto);
        }
        return achievementDtos;
    }

    public static StudentDetailDto toStudentDetailDto(Student student) {
        StudentDetailDto studentDetailDto = new StudentDetailDto();
        studentDetailDto.setGrade(student.getGrade());
        return studentDetailDto;
    }

    public static User toUser (User user, UpdateUserRequest req){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (req.getCreateAddressRequest()!=null){
            user.setAddress(toAddress(user.getAddress(),req.getCreateAddressRequest()));
        }
        if (req.getBirthday()!=null){
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            user.setBirthday(formatterDate.format(req.getBirthday()));
        }
        if (req.getGender()!=null){
            user.setGender(req.getGender());
        }

        if (req.getFullName()!=null){
            user.setFullName(req.getFullName());
        }
        if (req.getAvatar()!=null){

            // Create folder to save file if not exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            MultipartFile fileData = req.getAvatar();
            String name = fileData.getOriginalFilename() + user.getEmail();
            if (name != null && name.length() > 0) {
                try {
                    // Create file
                    String path = UPLOAD_DIR + "/" + name;
                    File serverFile = new File(path);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    user.setAvatar(path);
                } catch (Exception e) {
                    throw new InternalServerException("Error when uploading");
                }
            }
        }
        user.setUpdatedAt(formatter.format(new Date()));

        return user;
    }

    private static Address toAddress(Address address, CreateAddressRequest req) {
        if (req.getAddressDetail()!=null){
            address.setAddressDetail(req.getAddressDetail());
        }
        if (req.getWard()!=null){
            address.setWard(req.getWard());
        }
        if (req.getDistrict()!=null){
            address.setDistrict(req.getDistrict());
        }
        if (req.getPhoneNumber()!=null){
            address.setPhoneNumber(req.getPhoneNumber());
        }
        if (req.getProvince()!=null){
            address.setProvince(req.getProvince());
        }
        return address;
    }

}
