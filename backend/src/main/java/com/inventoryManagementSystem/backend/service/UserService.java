package com.inventoryManagementSystem.backend.service;


import com.inventoryManagementSystem.backend.entity.Supplier;
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
        System.out.println(user.getName());
        boolean exists2 = userRepository.existsByEmail(user.getEmail());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user);
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
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Utility.getResponse(new StatusEntry(ResponseEnum.RETRIEVED_SUCCESSFULLY), user);
            }else{
                return Utility.getResponse(new StatusEntry(ResponseEnum.FAILED), "Invalid password");
            }
        }
        return Utility.getResponse(new StatusEntry(ResponseEnum.NO_DATA), "User with name or email not found");
    }

}
