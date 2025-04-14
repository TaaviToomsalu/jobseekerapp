package com.taavi.jobseekerapp.dto;

import com.taavi.jobseekerapp.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}