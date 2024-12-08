package com.inventoryManagementSystem.backend.service;


import com.inventoryManagementSystem.backend.entity.User;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.entry.ResponseEnum;
import com.inventoryManagementSystem.backend.entry.StatusEntry;
import com.inventoryManagementSystem.backend.repository.UserRepository;
import com.inventoryManagementSystem.backend.utility.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CounterService counterService;

    public CommonResponse<List<User>> getAllUsers() {
        List<User> data = userRepository.findAll();
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY),data);
    }

    public CommonResponse<String> signUpUser(User user) {
        boolean exists1 = userRepository.existsByName(user.getName());
        boolean exists2 = userRepository.existsByEmail(user.getEmail());
        if (exists1) {
            String errorMessage = "User with name '" + user.getName() + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }
        if (exists2) {
            String errorMessage = "User with email '" + user.getEmail() + "' already exists";
            return Utility.getResponse(new StatusEntry(ResponseEnum.ALREADY_EXISTS), errorMessage);
        }

        // Generate sequence for the new supplier
        long sequence = counterService.generateSequence("userId");
        user.setUserId("USR-" + sequence);

        //Hash and set password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save supplier to the database
        userRepository.insert(user);
        String data = user.getName() + " added successfully";

        return Utility.getResponse(new StatusEntry(ResponseEnum.INSERTED_SUCCESSFULLY), data);
    }

    public CommonResponse<User> signInUser(String nameOrEmail,String password) {
        boolean exists1 = userRepository.existsByName(nameOrEmail);
        boolean exists2 = userRepository.existsByEmail(nameOrEmail);
        Optional<User> optionalUser=null;
        if (exists1) {
            optionalUser = userRepository.findByName(nameOrEmail);
        }
        if(exists2) {
            optionalUser = userRepository.findByEmail(nameOrEmail);
        }
        if(optionalUser!=null && optionalUser.isPresent()) {
            User user=optionalUser.get();
            if(!user.isActive())
            {
                return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "Your account is not approved");
            }
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), user);
            }else{
                return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "Invalid password");
            }
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User with name or email not found");
    }

    public CommonResponse<String> activateUser(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(true);
            userRepository.save(user);
            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), "User activated successfully");
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User not found");
    }

    /**
     * Deactivates a user by setting their isActive status to false.
     */
    public CommonResponse<String> deactivateUser(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userRepository.save(user);
            return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), "User deactivated successfully");
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User not found");
    }
    public CommonResponse<List<User>> getStaffUsers() {
        List<User> staffUsers = userRepository.findByRole("staff");
        if (staffUsers.isEmpty()) {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "No staff users found");
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), staffUsers);
    }

    public CommonResponse<List<User>> getUsersWithNullRole() {
        List<User> usersWithNullRole = userRepository.findByRoleIsNull();
        if (usersWithNullRole.isEmpty()) {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "No users with null role found");
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), usersWithNullRole);
    }

    public CommonResponse<String> approveUser(String userId, String role) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User not found with ID: " + userId);
        }

        User user = optionalUser.get();
        user.setRole(role);       // Set the role
        user.setActive(true);     // Set isActive to true
        userRepository.save(user);

        String successMessage = "User " + user.getName() + " has been approved with role: " + role;
        return Utility.getResponse(new StatusEntry(ResponseEnum.UPDATED_SUCCESSFULLY), successMessage);
    }

    public CommonResponse<String> declineUser(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User not found with ID: " + userId);
        }

        userRepository.deleteById(userId); // Delete the user
        String successMessage = "User with ID " + userId + " has been declined and removed.";
        return Utility.getResponse(new StatusEntry(ResponseEnum.DELETED_SUCCESSFULLY), successMessage);
    }

}
