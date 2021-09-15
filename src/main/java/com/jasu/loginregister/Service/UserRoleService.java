package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Entity.UserRole;

import java.util.Set;

public interface UserRoleService {

    boolean existUserRole(Long userId, String roleKey);

    void createUserRole(Long userId, String roleKey);

    Set<UserRole> getListUserRole(Long userId);
}
