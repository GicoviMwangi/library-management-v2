package com.library.app.service;

import com.library.app.model.Role;
import com.library.app.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;

    public Object saveRole(Role role) {
        return repository.save(role);
    }
}
