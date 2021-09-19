package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Address;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.InternalServerException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.BasicDto.UserDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.CreateAddressRequest;
import com.jasu.loginregister.Model.Request.CreatedToUser.CreateUserRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import com.jasu.loginregister.Repository.UserRepository;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

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
        if (req.getCreateAddressRequest()!=null){
            Address address = checkAddress(req.getCreateAddressRequest());
            user.get().setAddress(address);
        }
        if (req.getBirthday()!=null){
            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            user.get().setBirthday(formatterDate.format(req.getBirthday()));
        }
        if (req.getGender()!=null){
            user.get().setGender(req.getGender());
        }

        if (req.getFullName()!=null){
            user.get().setFullName(req.getFullName());
        }

        if (req.getAvatar()!=null){

            // Create folder to save file if not exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            MultipartFile fileData = req.getAvatar();
            String name = fileData.getOriginalFilename() + user.get().getEmail();
            if (name != null && name.length() > 0) {
                try {
                    // Create file
                    String path = UPLOAD_DIR + "/" + name;
                    File serverFile = new File(path);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    user.get().setAvatar(path);
                } catch (Exception e) {
                    throw new InternalServerException("Error when uploading");
                }
            }
        }
        return userRepository.saveAndFlush(user.get());
    }

    private Address checkAddress(CreateAddressRequest req) {
        Address address = new Address();
        if (!req.getAddressDetail().isEmpty()){
            address.setAddressDetail(req.getAddressDetail());
        }
        if (!req.getWard().isEmpty()){
            address.setWard(req.getWard());
        }
        if (!req.getDistrict().isEmpty()){
            address.setDistrict(req.getDistrict());
        }
        if (!req.getPhoneNumber().isEmpty()){
            address.setPhoneNumber(req.getPhoneNumber());
        }
        if (!req.getProvince().isEmpty()){
            address.setProvince(req.getProvince());
        }
        return address;
    }

    @Override
    public String deleteUser(Long id) {
        log.info("delete user by id in Service");
        Optional<User> result = userRepository.findById(id);
        if (!result.isPresent()){
            throw new NotFoundException("No user found");
        }
        result.get().setDeleted(true);
        User user = userRepository.saveAndFlush(result.get());
        return "Delete user success";
    }

    @Override
    public void updateUser(User checkUser) {
        userRepository.saveAndFlush(checkUser);
    }

    @Override
    public boolean refundUserBeRejected(List<Long> userIds, Long fee) {
        List<User> userList = userRepository.findAllById(userIds);
        if (userList.isEmpty()){
            return false;
        }
        for (User user: userList){
            user.setCoin(user.getCoin()+fee);
            userRepository.saveAndFlush(user);
        }
        return true;
    }

    @Override
    public User loginWithEmailAndPassword(String email, String password) {
        log.info("Login in Service");

        //check email exist
        User result = userRepository.findByEmail(email);

        //compare password
        if (result==null||!new BCryptPasswordEncoder().matches(password,result.getPassword())||result.getDeleted()){
            throw new NotFoundException("No user found");
        }

        //check state
        if (!result.getState().equals("LOGIN")){
            result.setState("LOGIN");
        }

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
        for (Long userId: userIds){
            listUser.add(userRepository.getById(userId));
        }
        return listUser;
    }

}
