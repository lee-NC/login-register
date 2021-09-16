package com.jasu.loginregister.Service;

import com.jasu.loginregister.Entity.Role;

public interface RoleService {
    Role findByRoleKey(String roleKey);
}
