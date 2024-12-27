package com.secure.log.service;

import com.secure.log.dto.UserDto;
import com.secure.log.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(Long userId, User user);

    User updateUserRole(Long userId, String roleName);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}