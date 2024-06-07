package org.sang.foodorderingweb.controller;

import org.sang.foodorderingweb.model.Food;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.request.CreateFoodRequest;
import org.sang.foodorderingweb.response.MessageResponse;
import org.sang.foodorderingweb.service.FoodService;
import org.sang.foodorderingweb.service.RestaurantService;
import org.sang.foodorderingweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

	private final FoodService foodService;
	private final UserService userService;
	private final RestaurantService restaurantService;

	@Autowired
	public AdminFoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
		this.foodService = foodService;
		this.userService = userService;
		this.restaurantService = restaurantService;
	}

	@PostMapping
	public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Restaurant restaurant=restaurantService.findRestaurantById(req.getRestaurantId());
		Food food=foodService.createFood(req,req.getCategory(),restaurant);
		return new ResponseEntity<>(food, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		foodService.deleteFood(id);
		MessageResponse res=MessageResponse.builder()
				.message("Food delete successful")
				.build();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long id,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Food food=foodService.updateAvailabilityStatus(id);
		return new ResponseEntity<>(food, HttpStatus.OK);
	}


}
