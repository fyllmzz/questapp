package com.project.questapp.controllers;

import com.project.questapp.entities.User;
import com.project.questapp.repos.UserRepository;
import com.project.questapp.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//ana request map
@RequestMapping("/user")

public class UserController {
    public UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping
    public User createUser(@RequestBody User newUser){
        return userService.saveOneUser(newUser);
    }
    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable Long userId){
        //custom exeption
        return  userService.getOneUserById(userId);
       // Optional<User> user=userRepository.findById(userId);
    }

    //varolan id üzerinde değişiklik yapılabililir putmapping sayesinde
    @PutMapping("/{userId}")
    public User updateOneUser(@PathVariable Long userId,@RequestBody User newUser){
      return  userService.updateOneUser(userId,newUser);
    }

    @DeleteMapping("/ {userId}")
    public  void deleteOneUser(@PathVariable Long userId){
       userService.deleteOneUser(userId);
    }

}

// en altta db ye bağlanan bi repository katmanı var
// repository den sonra servis katmanı var
// servis repodan bilgiyi alacak işleyecek üzerinde değişiklikler yapacak kontrol yapacak
//servis, son halini işlendikten sonraki halini controller a yollacak.
//busines logic tutulduğu servis katmanı olması lazım