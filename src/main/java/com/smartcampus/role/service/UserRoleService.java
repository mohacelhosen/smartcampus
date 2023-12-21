package com.smartcampus.role.service;

import com.smartcampus.role.model.UserRole;
import com.smartcampus.role.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository roleRepository;

    public UserRole registerNewRole(UserRole userRole){
        return roleRepository.save(userRole);
    }

    public List<UserRole> findAllRoles(){
        return roleRepository.findAll();
    }
}
