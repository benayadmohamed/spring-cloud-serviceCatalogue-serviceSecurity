package com.controllers;

import com.entities.Users;
import com.services.AccountService;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    AccountService accountService;
    @Value("${HEADER_STRING}")
    public String HEADER_STRING;

    @PostMapping(value = "sign-up")
    Users singUp(@RequestBody UserFrom userFrom) {
        System.out.println("userFrom = [" + userFrom + "]");
        return accountService.addUser(
                userFrom.username,
                userFrom.password,
                userFrom.confirm_password
        );
    }

    @RequestMapping("test")
    String test() {
        return HEADER_STRING;
    }

    @RequestMapping("users")
    String users() {
        return HEADER_STRING;
    }
}

@Data
@ToString
class UserFrom {
    String username;
    String password;
    String confirm_password;

}