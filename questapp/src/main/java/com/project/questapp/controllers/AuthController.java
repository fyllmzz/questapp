package com.project.questapp.controllers;

import com.project.questapp.request.UserRequest;
import com.project.questapp.responses.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.project.questapp.entities.User;

import com.project.questapp.security.JwtTokenProvider;

import com.project.questapp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private  JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private  PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
//        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());

        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken=jwtTokenProvider.generateJwtToken(authentication);
        User user= userService.getOneUserName(loginRequest.getUserName());
        AuthResponse authResponse= new AuthResponse();
        authResponse.setMessage("Bearer "+ jwtToken);
        authResponse.setUserId(user.getId());
        return authResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@RequestBody UserRequest registerRequest){
        AuthResponse authResponse=new AuthResponse();
        if(userService.getOneUserName(registerRequest.getUserName()) != null){
            authResponse.setMessage("Username already in use.");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            User user=new User();
            user.setUserName(registerRequest.getUserName());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            User savedUser = userService.saveOneUser(user);
            System.out.println("Saved User ID: " + savedUser.getId());



            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    registerRequest.getUserName(), registerRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            String jwtToken = jwtTokenProvider.generateJwtToken(authentication);
            authResponse.setMessage("Kullanıcı başarıyla kaydedildi.");
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            authResponse.setMessage("Kullanıcı kaydedilirken bir hata oluştu.");
            return new ResponseEntity<>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

       // return new ResponseEntity<>("User Successfully registered.", HttpStatus.CREATED);
    }






}