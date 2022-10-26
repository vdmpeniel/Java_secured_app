package com.vic.security.securedapp.service;

import com.vic.security.securedapp.entity.User;
import com.vic.security.securedapp.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);
}
