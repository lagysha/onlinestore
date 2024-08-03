package io.teamchallenge;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
    repositoryBaseClass = BaseJpaRepositoryImpl.class
)
@OpenAPIDefinition(
    servers = {
        @Server(url = "/", description = "Default Server URL")
    }
)
public class OnlineStoreApplication {
    /**
     * Main method of SpringBoot app.
     */
    public static void main(String[] args) {
        SpringApplication.run(OnlineStoreApplication.class, args);
    }
}