package com.info.repository;

import com.info.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {


}
