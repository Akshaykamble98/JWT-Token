package com.myfirst.project.iimpl;

import java.util.List;

import com.myfirst.project.entity.User;

public interface IUserImpl {
	
	
	List<User> findAll();
	
	User findById(Long id);
	
	User save(User user);
	
	void deleteUserById(Long id);
	

}
