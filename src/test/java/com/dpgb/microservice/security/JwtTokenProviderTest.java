package com.dpgb.microservice.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;


@RunWith(SpringRunner.class)
@WebMvcTest(JwtTokenProvider.class)
public class JwtTokenProviderTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    MyUserDetails myUserDetails;

    @Test
    public void createToken() throws UnsupportedEncodingException {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        String token = jwtTokenProvider.createToken("ADMIN","ADMIN");
        assert(token).length()>0;
        System.out.println(token);
    }
}