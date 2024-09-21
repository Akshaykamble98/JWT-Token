package com.myfirst.project.resourse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfirst.project.entity.User;
import com.myfirst.project.iimpl.IUserImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserResource {
	
    @Autowired
    private IUserImpl iUserImpl;
    
    @GetMapping
    public List<User> getAllUsers() {
        return (List<User>) iUserImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = iUserImpl.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping("/id")
    public ResponseEntity<User> getUserByIdFromRequestBody(@Valid @RequestBody User user){
        User userToReturn = iUserImpl.findById(user.getId());
        return new ResponseEntity<>(userToReturn, HttpStatus.OK);
    }
    
    @PostMapping("/save")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = iUserImpl.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUserById(@Valid @RequestBody User user) {
    	iUserImpl.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
	

}
