package com.alexnv.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    @NotBlank(message = "Name is mandatory")
    private String firstname;

    @Column(name = "lastname")
    @NotBlank(message = "Name is mandatory")
    private String lastname;

    @Column(name = "age")
    @NotBlank(message = "Name is mandatory")
    private String age;

    @Column(name = "email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Name is mandatory") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotBlank(message = "Name is mandatory") String firstname) {
        this.firstname = firstname;
    }

    public @NotBlank(message = "Name is mandatory") String getLastname() {
        return lastname;
    }

    public void setLastname(@NotBlank(message = "Name is mandatory") String lastname) {
        this.lastname = lastname;
    }

    public @NotBlank(message = "Name is mandatory") String getAge() {
        return age;
    }

    public void setAge(@NotBlank(message = "Name is mandatory") String age) {
        this.age = age;
    }

    public void setPassword(@NotBlank(message = "Password is mandatory") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public @NotBlank(message = "Password is mandatory") String getPassword() {
        return password;
    }

    @Override
    public @NotBlank(message = "Name is mandatory") String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
