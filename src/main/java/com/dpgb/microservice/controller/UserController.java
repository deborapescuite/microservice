package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET, value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = GET, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = DELETE, value = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = PUT, value = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User updatedUser) {
        return userService.update(id, updatedUser);
    }

    @RequestMapping(method = POST, value = "/user/signin")
    public String login(@RequestBody User createUser) {
        return userService.signin(createUser.getName(), createUser.getPassword());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/signup")
    public String signup(@RequestBody User createUser) {
        return userService.signup(createUser);
    }

    @RequestMapping(method = POST, value = "/user/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public String refreshToken(@RequestBody String originalToken) {
        return userService.refresh(originalToken);
    }
}
