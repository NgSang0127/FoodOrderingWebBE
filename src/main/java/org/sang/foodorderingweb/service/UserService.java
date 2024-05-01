package org.sang.foodorderingweb.service;

import org.sang.foodorderingweb.model.User;

public interface UserService {
	User findUserByJwtToken(String jwt) throws Exception;

	User findUserByEmail(String email) throws  Exception;


}
