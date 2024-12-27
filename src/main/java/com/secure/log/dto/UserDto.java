package com.secure.log.dto;

import com.secure.log.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private String email;
    private Role role;

}