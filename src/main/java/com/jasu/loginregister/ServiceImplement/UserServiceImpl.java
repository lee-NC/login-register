package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Email.EmailService;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.BasicDto.UserDto;
import com.jasu.loginregister.Model.Mapper.UserDetailMapper;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import com.jasu.loginregister.Repository.UserRepository;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.USER_REJECTED_CONTENT;
import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.USER_REJECTED_SUBJECT;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDto createUser(User user) {
        // Check email exist
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateRecordException("Email is already in use");
        }
        return UserMapper.toUserDto(userRepository.saveAndFlush(user));
    }

    @Override
    public User updateDetailUser(UpdateUserRequest req, Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new NotFoundException("No user found");
        }
        User saveUser = new User();
        try {
            saveUser = userRepository.saveAndFlush(UserDetailMapper.toUser(user.get(),req));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return saveUser;
    }

    @Override
    public String deleteUser(Long id) {
        log.info("delete user by id in Service");
        Optional<User> result = userRepository.findById(id);
        if (!result.isPresent()){
            throw new NotFoundException("No user found");
        }
        result.get().setDeleted(true);
        try {
            userRepository.saveAndFlush(result.get());
            return "Delete user successfully";
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "Delete user unsuccessfully";
    }

    @Override
    public void updateUser(User checkUser) {
        try {
            userRepository.saveAndFlush(checkUser);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean refundUserBeRejected(List<Long> userIds, Long fee) {
        List<User> userList = userRepository.findAllById(userIds);
        if (userList.isEmpty()){
            return false;
        }
        try {
            for (User user: userList){
                user.setCoin(user.getCoin()+fee);
                emailService.sendAnEmail(user.getEmail(),USER_REJECTED_CONTENT,USER_REJECTED_SUBJECT);
                userRepository.saveAndFlush(user);
            }
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public User findByEmail(String email) {
        log.info("Find user by email in Service");
        User result = userRepository.findByEmail(email);
        if (result==null){
            throw new NotFoundException("No user found");
        }
        return result;
    }

//    @Override
//    public boolean verifyUser(String code) {
//        User user = userRepository.findByVerificationCode(code);
//
//        if (user == null || user.isEnabled()) {
//            return false;
//        } else {
//            user.setVerificationCode(null);
//            user.setEnabled(true);
//            userRepository.saveAndFlush(user);
//            return true;
//        }
//    }

    @Override
    public User loginWithEmailAndPassword(String email, String password) {
        log.info("Login in Service");
        
        try {
            //check email exist
            User result = userRepository.findByEmail(email);

            //compare password
            if (result==null){
                throw new NotFoundException("Wrong password or username");
            }

            if (!new BCryptPasswordEncoder().matches(password,result.getPassword())||result.getDeleted()){
                throw new NotFoundException("Wrong password or username");
            }

            //check state
            if (result.getState().equals("LOGIN")){
                throw new ForbiddenException("You logged in before. Do you want to log out?");
            }

            result.setState("LOGIN");
            //check active time to count num active
            if (result.getNumActive()<=7 && checkTime(result.getLastActive())==1)    {
                result.setNumActive(result.getNumActive()+1);
            }
            else {
                result.setNumActive(1);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            result.setLastActive(formatter.format(date));
            result.setLastLogin(formatter.format(date));
            return   userRepository.saveAndFlush(result);
        }catch (Exception e){
            System.out.println(e.getMessage());

            throw new ForbiddenException("ACCESS DENIED");
        }
    }

    //check time between two days
    private long checkTime(String lastActiveTime){
        log.info("Check many times user login in one day in Service");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String nowTime = formatter.format(date);
        String []lastTime = lastActiveTime.split(" ");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate lastDate = LocalDate.parse(lastTime[0], dateTimeFormatter);
        LocalDate nowDate = LocalDate.parse(nowTime, dateTimeFormatter);
        return ChronoUnit.DAYS.between(lastDate, nowDate);
    }

    @Override
    public User findByID(Long userCreateId) {
        log.info("Find user by id in Service");
        Optional<User> result = userRepository.findById(userCreateId);
        if (!result.isPresent()){
            throw new NotFoundException("No user found");
        }
        return result.get();
    }

    @Override
    public List<User> getListUser(List<Long> userIds) {
        List<User> listUser = new ArrayList<>();
        try {
            for (Long userId: userIds){
                listUser.add(userRepository.getById(userId));
            }
        }catch (Exception e){
            throw new NotFoundException("no user found");
        }
        return listUser;
    }

}
