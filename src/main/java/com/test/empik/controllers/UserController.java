package com.test.empik.controllers;


import com.test.empik.payload.response.UserDetailsResponse;
import com.test.empik.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{login}")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable(value = "login") String login) {
        UserDetailsResponse userDetailsResponse = userService.getUserDetails(login);
        return ResponseEntity.ok(userDetailsResponse);
    }

}
