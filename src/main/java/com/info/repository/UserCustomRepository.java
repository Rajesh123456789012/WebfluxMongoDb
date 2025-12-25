package com.info.repository;

import com.info.entity.UserEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserCustomRepository {

    private final DatabaseClient client;

    public UserCustomRepository(DatabaseClient client) {
        this.client = client;
    }

    public Flux<UserEntity> findAllPaged(int page, int size) {
        int offset = page * size;

        return client.sql("""
                SELECT * FROM user_info
                ORDER BY id
                LIMIT :size OFFSET :offset
                """)
                .bind("size", size)
                .bind("offset", offset)
                .map((row, meta) -> {
                    UserEntity user = new UserEntity();
                    user.setId(row.get("id", Long.class));
                    user.setName(row.get("name", String.class));
                    user.setEmail(row.get("email", String.class));
                    return user;
                })
                .all();
    }

    public Mono<Long> count() {
        return   client.sql("SELECT COUNT(*) FROM user_info")
                .map(row -> row.get(0, Long.class))
                .one();
    }
}
