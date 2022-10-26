package com.vic.security.securedapp.controller;
import com.vic.security.securedapp.entity.User;
import com.vic.security.securedapp.event.RegistrationCompleteEvent;
import com.vic.security.securedapp.model.UserModel;
import com.vic.security.securedapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, getApplicationUrl(request)));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        return (result.equalsIgnoreCase("valid"))?
                "User Verifies Successfully"
                : "Bad User";
    }


    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();
    }


}
