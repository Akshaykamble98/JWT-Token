package com.myfirst.project.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfirst.project.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String email);

}
