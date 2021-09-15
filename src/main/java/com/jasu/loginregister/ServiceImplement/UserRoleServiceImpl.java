package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Entity.UserRole;
import com.jasu.loginregister.Exception.DuplicateRecordException;
import com.jasu.loginregister.Exception.ForbiddenException;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.UserRoleRepository;
import com.jasu.loginregister.Service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public boolean existUserRole(Long userId, String roleKey) {
        log.info("Check user role in service");
        boolean checkUserRole = userRoleRepository.existsByUserIdAndRoleKey(userId,roleKey);
        return checkUserRole;
    }

    @Override
    public void createUserRole(Long userId, String roleKey) {
        log.info("create user role in service");
        UserRole userRole = new UserRole(userId,roleKey);
        userRoleRepository.save(userRole);
    }

    @Override
    public Set<UserRole> getListUserRole(Long userId) {
        log.info("Get list user role in service");
        Set<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        return userRoles;
    }
}
