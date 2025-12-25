package com.info.service;

import com.info.entity.UserEntity;
import com.info.model.PageResponse;
import com.info.model.User;
import com.info.repository.UserCustomRepository;
import com.info.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Profile("local")
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCustomRepository repo;

    public Mono<UserEntity> saveUser(User user){

        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
      return   userRepository.save(entity)
              .map(userEntity -> {
                  System.out.println("successfully saved:"+userEntity.getId());
                  return userEntity;
              }).onErrorResume(throwable -> {
                  System.out.println("error occurred:"+throwable.getMessage());
                return   Mono.error(throwable);
              });
    }

    public Mono<PageResponse<UserEntity>> getUsers(int page, int size) {
        return Mono.zip(
                repo.findAllPaged(page, size).collectList(),
                repo.count()
        ).map(tuple -> new PageResponse<>(
                tuple.getT1(),
                page,
                size,
                tuple.getT2()
        ));
    }
}
