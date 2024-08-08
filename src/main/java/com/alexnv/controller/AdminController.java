package com.alexnv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.alexnv.model.User;
import com.alexnv.model.Role;
import com.alexnv.service.RoleServiceImpl;
import com.alexnv.service.UserServiceImpl;
import com.alexnv.exception.UserNotFoundException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        List<User> users = userServiceImpl.findAllUsers();
        Map<Long, String> userRolesMap = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user.getRoles().stream()
                                .map(Role::getRole)
                                .collect(Collectors.joining(", "))
                ));

        model.addAttribute("user", userServiceImpl.findByEmail(principal.getName()));
        model.addAttribute("users", users);
        model.addAttribute("userRolesMap", userRolesMap);
        model.addAttribute("roles", roleServiceImpl.findAllRoles());
        return "admin";
    }


    @PostMapping("/new")
    public String addNewUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        Set<Role> roles = roleServiceImpl.findRolesByIds(roleIds);
        user.setRoles(roles);
        userServiceImpl.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userServiceImpl.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PatchMapping("/user/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @ModelAttribute User user,
            @RequestParam("roles") Set<Long> roleIds) {

        User existingUser = userServiceImpl.findById(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }

        Set<Role> roles = roleServiceImpl.findRolesByIds(roleIds);

        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setRoles(roles);

        userServiceImpl.updateUser(id, existingUser, roles);

        return "redirect:/admin";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userServiceImpl.findById(id);
        Set<Role> allRoles = roleServiceImpl.findAllRoles();

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("allRoles", allRoles);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImpl.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
