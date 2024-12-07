package com.inventoryManagementSystem.backend.controller;


import com.inventoryManagementSystem.backend.entity.Supplier;
import com.inventoryManagementSystem.backend.entity.User;
import com.inventoryManagementSystem.backend.entry.CommonResponse;
import com.inventoryManagementSystem.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PutMapping("/activate-user")
    public ResponseEntity<CommonResponse<String>> activateUser(@RequestBody String userId) {
        return new ResponseEntity<>(userService.activateUser(userId), HttpStatus.OK);
    }

    @PutMapping("/deactivate-user")
    public ResponseEntity<CommonResponse<String>> deactivateUser(@RequestBody String userId) {
        return new ResponseEntity<>(userService.deactivateUser(userId), HttpStatus.OK);
    }

    @GetMapping("/staff")
    public ResponseEntity<CommonResponse<List<User>>> getStaffUsers() {
        return ResponseEntity.ok(userService.getStaffUsers());
    }

    @GetMapping("/no-role")
    public ResponseEntity<CommonResponse<List<User>>> getUsersWithNullRole() {
        return ResponseEntity.ok(userService.getUsersWithNullRole());
    }

    @PutMapping("/approve-user/{id}")
    public ResponseEntity<CommonResponse<String>> approveUser(@RequestBody String role,@PathVariable String id) {
        return new ResponseEntity<>(userService.approveUser(id,role), HttpStatus.OK);
    }

    @DeleteMapping("/decline-user/{id}")
    public ResponseEntity<CommonResponse<String>> declineUser(@PathVariable String id) {
        return new ResponseEntity<>(userService.declineUser(id), HttpStatus.OK);
    }
}
