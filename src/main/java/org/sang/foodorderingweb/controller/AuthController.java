package org.sang.foodorderingweb.controller;

import java.util.Collection;
import org.sang.foodorderingweb.config.JwtProvider;
import org.sang.foodorderingweb.model.Cart;
import org.sang.foodorderingweb.model.USER_ROLE;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.repository.CartRepository;
import org.sang.foodorderingweb.repository.UserRepository;
import org.sang.foodorderingweb.request.LoginRequest;
import org.sang.foodorderingweb.response.AuthResponse;
import org.sang.foodorderingweb.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final CustomerUserDetailsService customerUserDetailsService;
	private final CartRepository cartRepo;

	@Autowired
	public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
			CustomerUserDetailsService customerUserDetailsService, CartRepository cartRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider = jwtProvider;
		this.customerUserDetailsService = customerUserDetailsService;
		this.cartRepo = cartRepo;
	}

	//Register method
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
		User isEmailExists = userRepo.findByEmail(user.getEmail());
		if (isEmailExists != null) {
			throw new Exception("Email is already used with another account");
		}
		User createdUser = new User();
		createdUser.setEmail(user.getEmail());
		createdUser.setFullName(user.getFullName());
		createdUser.setRole(user.getRole());
		createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

		User savedUser = userRepo.save(createdUser);

		Cart cart = new Cart();
		cart.setCustomer(savedUser);
		cartRepo.save(cart);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = AuthResponse.builder()
				.jwt(jwt)
				.message("Register success")
				.role(user.getRole())
				.build();
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	//Log in method
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req) {
		String username = req.getEmail();
		String password = req.getPassword();
		Authentication authentication = authenticate(username, password);
		String jwt = jwtProvider.generateToken(authentication);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		AuthResponse authResponse = AuthResponse.builder()
				.jwt(jwt)
				.message("Login success")
				.role(USER_ROLE.valueOf(role))
				.build();
		return new ResponseEntity<>(authResponse, HttpStatus.OK);

	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username ...");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password ...");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
