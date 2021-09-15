package com.jasu.loginregister.Controller;

import com.jasu.loginregister.Entity.*;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Dto.DetailDto.StudentDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.TutorDetailDto;
import com.jasu.loginregister.Model.Dto.DetailDto.UserDetailDto;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateTutorRequest;
import com.jasu.loginregister.Model.Request.UpdateToUser.UpdateUserRequest;
import com.jasu.loginregister.Service.StudentService;
import com.jasu.loginregister.Service.TutorService;
import com.jasu.loginregister.Service.UserRoleService;
import com.jasu.loginregister.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";

    @Autowired
    private UserService userService;

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename) {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new NotFoundException("File not found");
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            throw new NotFoundException("File not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }


}
