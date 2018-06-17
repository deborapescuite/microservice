package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Audit;
import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.UserNotFoundException;
import com.dpgb.microservice.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @RequestMapping(method = GET, value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer id) {
        logger.info("GET a user with id: " + id);
        return userService.findById(id);
    }

    @RequestMapping(method = GET, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        logger.info("GET all users created.");
        return userService.findAll();
    }

    @RequestMapping(method = DELETE, value = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        logger.info("DELETE  a user with id: " + id);
        userService.delete(id);
    }

    @RequestMapping(method = PUT, value = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User updatedUser) {
        logger.info("UPDATE/PUT  a product with id: " + id);
        return userService.update(id, updatedUser);
    }

    @RequestMapping(method = POST, value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody User createUser, UriComponentsBuilder ucb) {
        logger.info("CREATE/POST  a new user.");
        User newUser = userService.save(createUser);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri =
                ucb.path("/user/")
                        .path(String.valueOf(newUser.getId()))
                        .build()
                        .toUri();
        headers.setLocation(locationUri);
        ResponseEntity<User> responseEntity =
                new ResponseEntity<User>(
                        newUser, headers, HttpStatus.CREATED);
        return responseEntity;
    }
}
