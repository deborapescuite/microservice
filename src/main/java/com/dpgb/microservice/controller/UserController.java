package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.Audit;
import com.dpgb.microservice.entity.Product;
import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.ProductNotFoundException;
import com.dpgb.microservice.exception.UserNotFoundException;
import com.dpgb.microservice.repository.AuditRepository;
import com.dpgb.microservice.repository.UserRepository;
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
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuditRepository auditRepository;

    @RequestMapping(method = GET, value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer id) {
        logger.info("GET a user with id: " + id);
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new UserNotFoundException("User id: " + id + " not found.");
    }

    @RequestMapping(method = GET, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        logger.info("GET all users created.");
        return userRepository.findAll();
    }

    @RequestMapping(method = DELETE, value = "/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        logger.info("DELETE  a user with id: " + id);
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            //TO DO : add user id
            auditRepository.save(new Audit("Delete user", id, LocalTime.now(), null));
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be deleted.");
    }

    @RequestMapping(method = PUT, value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody User updatedUser) {
        logger.info("UPDATE/PUT  a product with id: " + id);
        if (userRepository.findById(id).isPresent()){
            //TO DO : add user id
            auditRepository.save(new Audit("Update user", id, LocalTime.now(), null));
            return userRepository.save(updatedUser);
        }
        else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be updated.");
    }

    @RequestMapping(method = POST, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<User>  createUser(@Valid @RequestBody User createUser, UriComponentsBuilder ucb) {
        logger.info("CREATE/POST  a new user.");
        User newUser = userRepository.save(createUser);

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
