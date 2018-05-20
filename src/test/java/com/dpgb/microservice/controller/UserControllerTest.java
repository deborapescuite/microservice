package com.dpgb.microservice.controller;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.repository.AuditRepository;
import com.dpgb.microservice.repository.UserRepository;
import com.dpgb.microservice.utils.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuditRepository auditRepository;

    ObjectMapper mapper = new ObjectMapper();

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setName("User1");
        user.setCreateDate(new Date());
        //user.setCreationDate(LocalDateTime.now());
        user.setUserType(UserType.ADMIN);
    }

    public byte[] serializeUser(User user) throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(user);
    }

    @Test
    public void createUser() throws Exception {

        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeUser(this.user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createProductWithError() throws Exception {
        User emptyUser = new User();

        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeUser(emptyUser)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUser() throws Exception {

        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));

        mvc.perform(
                put("/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializeUser(this.user)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUser() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));

        mvc.perform(get("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.user.getId()))
                .andExpect(jsonPath("$.name").value("User1"))
                .andExpect(jsonPath("$.userType").value(UserType.ADMIN.toJson()));
    }


    @Test
    public void getAllUsers() throws Exception {
        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(this.user));
        doNothing().when(userRepository).deleteById(this.user.getId());

        mvc.perform(
                delete("/user/{id}", this.user.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}