package com.vic.security.securedapp.service;

import com.vic.security.securedapp.entity.User;
import com.vic.security.securedapp.entity.VerificationToken;
import com.vic.security.securedapp.model.UserModel;
import com.vic.security.securedapp.repository.UserRepository;
import com.vic.security.securedapp.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel){
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user){
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(token);
        if(!optionalVerificationToken.isPresent()){ return "invalid"; }

        VerificationToken verificationToken = optionalVerificationToken.get();
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        boolean isExpired = (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0);
        if(isExpired) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
