package com.secure.log.service;

import com.secure.log.dto.UserDto;
import com.secure.log.enums.AppRole;
import com.secure.log.exception.ResourceAlreadyExistsException;
import com.secure.log.exception.ResourceNotFoundException;
import com.secure.log.model.Role;
import com.secure.log.model.User;
import com.secure.log.repository.RoleRepository;
import com.secure.log.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new ResourceAlreadyExistsException("User already exists with username: " + user.getUserName());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + user.getEmail());
        }

        Role role = roleRepository.findByRoleName(user.getRole().getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + user.getRole().getRoleName()));
        user.setRole(role);

        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(role);
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = findUserById(userId);
        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    @Override
    public User updateUserRole(Long userId, String roleName) {
        User existingUser = findUserById(userId);

        AppRole appRole;
        try {
            appRole = AppRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        existingUser.setRole(role);
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}