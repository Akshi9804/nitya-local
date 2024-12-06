package com.inventoryManagementSystem.backend.controller;


import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entity.User;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<CommonResponse<User>> signInUser(@RequestBody Map<String,String> credentials) {
        return new ResponseEntity<>(userService.signInUser(credentials.get("nameOrEmail"),credentials.get("password")), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signUpUser(@RequestBody User user) {
        System.out.println(user);
        return new ResponseEntity<>(userService.signUpUser(user), HttpStatus.OK);
    }
}
