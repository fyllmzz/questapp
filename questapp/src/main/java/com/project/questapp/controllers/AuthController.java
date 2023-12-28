package com.project.questapp.controllers;

import com.project.questapp.request.UserRequest;
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
    public String login(@RequestBody UserRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());

        Authentication authentication=authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken=jwtTokenProvider.generateJwtToken(authentication);
        return "Bearer "+ jwtToken;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody UserRequest registerRequest){
        if(userService.getOneUserName(registerRequest.getUserName()) != null){
            return new ResponseEntity<>("Username already in use.", HttpStatus.BAD_REQUEST);
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

            return new ResponseEntity<>("Kullanıcı başarıyla kaydedildi. Bearer " + jwtToken, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Kullanıcı kaydedilirken bir hata oluştu.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

       // return new ResponseEntity<>("User Successfully registered.", HttpStatus.CREATED);
    }






}