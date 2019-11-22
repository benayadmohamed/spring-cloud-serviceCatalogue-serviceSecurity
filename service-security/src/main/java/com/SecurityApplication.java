package com;

import com.entities.Roles;
import com.entities.Users;
import com.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AccountService accountService) {
        return args -> {
            accountService.deleteALL();
            Roles roles = new Roles("ADMIN");
            Roles roles2 = new Roles("USER");
            roles = accountService.addRole(roles);
            roles2 = accountService.addRole(roles2);
            Users users = accountService.addUser("admin", "admin", "admin");
            accountService.addRoleToUser(users.getUsername(), "ADMIN");
        };
    }

}
