package com.jasu.loginregister.ServiceImplement;

import com.jasu.loginregister.Entity.Role;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Repository.RoleRepository;
import com.jasu.loginregister.Service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRoleKey(String authority) {
        Role role = roleRepository.findByRoleKey(authority);
        if (role==null){
            throw new NotFoundException("No role found");
        }
        return role;
    }
}
