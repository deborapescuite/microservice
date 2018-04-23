package com.dpgb.microservice.controller;

import com.dpgb.microservice.MicroserviceApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private static final Logger logger = LogManager.getLogger(MainController.class);
    @RequestMapping("/index")
    public String index(){
        logger.debug("at index()");
        return "Greetings!";
    }
}
