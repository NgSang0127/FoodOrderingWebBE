package org.sang.foodorderingweb.response;

import lombok.Builder;
import lombok.Data;
import org.sang.foodorderingweb.model.USER_ROLE;
import org.sang.foodorderingweb.model.User;

@Data
@Builder
public class AuthResponse {
	private String jwt;
	private String message;
	private USER_ROLE role;

}
