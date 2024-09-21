package com.myfirst.project.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myfirst.project.entity.User;
import com.myfirst.project.exception.ResourceNotFoundException;
import com.myfirst.project.iimpl.IUserImpl;
import com.myfirst.project.repo.UserRepo;

@Service
public class UserImpl implements IUserImpl {
	
	@Autowired
	UserRepo userRepo;
	
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public UserImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public List<User> findAll() {
		return (List<User>) userRepo.findAll();
	}
	
	@Override
    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

	@Override
	public User save(User user) {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user); 
	}

	@Override
	public void deleteUserById(Long id) {
		 User user = findById(id);
		 userRepo.delete(user);
	}


}
