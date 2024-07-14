package io.teamchallenge;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
    repositoryBaseClass = BaseJpaRepositoryImpl.class
)
public class DaoApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(DaoApplicationTest.class, args);
    }
}