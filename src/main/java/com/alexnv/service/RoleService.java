package com.alexnv.service;

import com.alexnv.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> findAllRoles();
    Set<Role> findRolesByIds(Set<Long> roleIds);
}
