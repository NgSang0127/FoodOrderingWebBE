package org.sang.foodorderingweb.controller;

import java.util.List;
import org.sang.foodorderingweb.dto.RestaurantDTO;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.service.RestaurantService;
import org.sang.foodorderingweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;

	private final UserService userService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService, UserService userService) {
		this.restaurantService = restaurantService;
		this.userService = userService;
	}

	@GetMapping("/search")
	public ResponseEntity<List<Restaurant>> searchRestaurant(
			@RequestHeader("Authorization") String jwt,
			@RequestParam String keyword
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<Restaurant>> getAllRestaurants(
			@RequestHeader("Authorization") String jwt
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Restaurant> restaurant = restaurantService.getAllRestaurants();
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> findRestaurantById(
			@RequestHeader("Authorization") String jwt,
			@PathVariable Long id
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Restaurant restaurant = restaurantService.findRestaurantById(id);
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}

	@PutMapping("/{id}/add-favorites")
	public ResponseEntity<RestaurantDTO> addToFavorites(
			@RequestHeader("Authorization") String jwt,
			@PathVariable Long id
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		RestaurantDTO restaurant = restaurantService.addToFavorites(id, user);
		return new ResponseEntity<>(restaurant, HttpStatus.OK);
	}
}
