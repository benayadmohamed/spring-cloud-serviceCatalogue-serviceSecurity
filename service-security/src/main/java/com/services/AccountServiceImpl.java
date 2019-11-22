package com.services;

import com.entities.Roles;
import com.entities.Users;
import com.repositories.RoleRepository;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void deleteALL() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Override
    public Users addUser(String username, String password, String confirmPassword) {
//        System.out.println("username = [" + username + "], password = [" + password + "], confirmPassword = [" + confirmPassword + "]");
        Users users = userRepository.findUsersByUsername(username);
        if (users != null) throw new RuntimeException("user alredy exist");
        if (!password.equals(confirmPassword))
            throw new RuntimeException("the password value is not the same as confirmPassword");
        Users users1 = new Users();
        users1.setUsername(username);
        users1.setActived(true);

        users1.setPassword(bCryptPasswordEncoder.encode(password));

        users1 = userRepository.save(users1);
        return addRoleToUser(username, "USER");
    }

    @Override
    public Roles addRole(Roles role) {
        return roleRepository.save(role);
    }

    @Override
    public Users addRoleToUser(String username, String role) {
        Roles roles = roleRepository.findRolesByRole(role);
        Users users = userRepository.findUsersByUsername(username);
        users.getRoles().add(roles);
        return users;
    }

    @Override
    public Users loadUserbyUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }
}
