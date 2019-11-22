package com.repositories;

import com.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users, Long> {

    Users findUsersByUsername(String username);
}
