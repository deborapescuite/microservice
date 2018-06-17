package com.dpgb.microservice.service;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.exception.UserNotFoundException;
import com.dpgb.microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

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
            //TO DO : Audit
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be deleted.");
    }

    public User update(Integer id, User updatedUser) {
        if (userRepository.findById(id).isPresent()) {
            //TO DO : Audit
            return userRepository.save(updatedUser);
        } else
            throw new UserNotFoundException("User id: " + id + " not found.Could not be updated.");
    }

    public User save(User createUser) {
        return userRepository.save(createUser);
    }
}
