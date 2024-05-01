package org.sang.foodorderingweb.service;

import org.sang.foodorderingweb.config.JwtProvider;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{
	private final UserRepository userRepo;
	private final JwtProvider jwtProvider;

	@Autowired
	public UserServiceImp(UserRepository userRepo, JwtProvider jwtProvider) {
		this.userRepo = userRepo;
		this.jwtProvider = jwtProvider;
	}



	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email=jwtProvider.getEmailFromJwtToken(jwt);
		User user=findUserByEmail(email);
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user=userRepo.findByEmail(email);
		if(user==null){
			throw new Exception("User not found");
		}
		return user;
	}
}
