package com.info.controller;

import com.info.entity.UserEntity;
import com.info.model.PageResponse;
import com.info.model.User;
import com.info.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@Profile("local")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public Mono<UserEntity> createUser(@RequestBody User user){
        System.out.println("user:{}"+ user);
  return userService.saveUser(user);
    }

    @GetMapping("/users")
    public Mono<PageResponse<UserEntity>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        return userService.getUsers(page, size);
    }

}
