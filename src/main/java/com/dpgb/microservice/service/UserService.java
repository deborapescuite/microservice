package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.InvalidUsePasswordException;
import com.dpgb.microservice.exception.UserAlreadyExistsException;
import com.dpgb.microservice.exception.UserNotFoundException;
import com.dpgb.microservice.repository.UserRepository;
import com.dpgb.microservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User findById(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new UserNotFoundException("User id: " + id + " not found.");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be deleted.");
    }

    public User update(Integer id, User updatedUser) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.save(updatedUser);
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be updated.");
    }

    public String signin(String username, String password) {
        //TO DO : authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByName(username);
        String token = "";
        if (user != null) {
            if (user.getPassword().equals(password) && user.getName().equals(username))
                token = jwtTokenProvider.createToken(user.getName(), user.getUserType().name());
            else
                throw new InvalidUsePasswordException("Invalid username/password supplied");
        } else
            throw new InvalidUsePasswordException("Invalid username/password supplied");
        return token;
    }

    public String signup(User user) {
        if (!userRepository.existsByName(user.getName())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getName(), user.getUserType().name());
        } else {
            throw new UserAlreadyExistsException("Username is already in use");
        }
    }

}
