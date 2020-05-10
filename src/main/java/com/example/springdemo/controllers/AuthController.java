package com.example.springdemo.controllers;

import com.example.springdemo.model.requests.LoginRequest;
import com.example.springdemo.model.responses.JwtAuthenticationResponse;
import com.example.springdemo.model.user.UserRepository;
import com.example.springdemo.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity signIn(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(),
                  loginRequest.getPassword()
          )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        logger.info("Get token: " + token);
        return new ResponseEntity(
                new JwtAuthenticationResponse(token),
                HttpStatus.OK
        );

    }

}
