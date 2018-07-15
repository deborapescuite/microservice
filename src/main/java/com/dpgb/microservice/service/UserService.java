package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.InvalidUserPasswordException;
import com.dpgb.microservice.exception.NotFoundException;
import com.dpgb.microservice.exception.UserAlreadyExistsException;
import com.dpgb.microservice.exception.UserNotFoundException;
import com.dpgb.microservice.repository.UserRepository;
import com.dpgb.microservice.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public User findById(Integer id) {
        logger.info("Find user by id: " + id);
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new UserNotFoundException("User id: " + id + " not found.");
    }

    public List<User> findAll() {
        logger.info("Find all users." );
        return userRepository.findAll();
    }

    public void delete(Integer id) {
        logger.info("Delete user - id: " + id );
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be deleted.");
    }

    public User update(Integer id, User updatedUser) {
        logger.info("Update user - id: " + id );
        if (userRepository.findById(id).isPresent()) {
            return userRepository.save(updatedUser);
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be updated.");
    }

    public String signin(String username, String password) {
        logger.info("Sign in user: " + username);
        //TO DO : authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByName(username);
        String token = "";
        if (user != null) {
            if (user.getPassword().equals(password) && user.getName().equals(username))
                token = jwtTokenProvider.createToken(user.getName(), user.getUserType().name());
            else
                throw new InvalidUserPasswordException("Invalid username/password supplied");
        } else
            throw new InvalidUserPasswordException("Invalid username/password supplied");
        return token;
    }

    public String signup(User user) {
        logger.info("Sign up user: " + user.getName());
        if (!userRepository.existsByName(user.getName())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getName(), user.getUserType().name());
        } else {
            throw new UserAlreadyExistsException("Username is already in use");
        }
    }

    public User findByName(String name){
        return userRepository.findByName(name);
    }

    public String refresh(String originalToken) {
        String username = new String();
        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();

        if (originalToken == null)
            throw new NotFoundException("Authorization Token not found.");
        else {
            String[] parts = originalToken.split("\\."); // Splitting header, payload and signature
            String payload = new String(decoder.decode(parts[1])); // Payload
            try {
                JsonNode node = mapper.readTree(payload);
                username = node.get("sub").asText();
            } catch (IOException e) {
                logger.error("Error while retrieve username.");
                e.printStackTrace();
            }
            User user = userRepository.findByName(username);
            if (user != null)
                return jwtTokenProvider.createToken(user.getName(), user.getUserType().name());
            else
                throw new UserNotFoundException("User: " + "" + " not found.Could not refresh token.");
        }
    }

}
