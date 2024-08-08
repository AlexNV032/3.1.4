package com.alexnv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alexnv.model.Role;
import com.alexnv.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> findAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    public Set<Role> findRolesByIds(Set<Long> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}
