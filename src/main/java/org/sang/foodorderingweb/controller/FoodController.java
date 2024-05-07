package org.sang.foodorderingweb.controller;

import java.util.List;
import org.sang.foodorderingweb.model.Food;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.service.FoodService;
import org.sang.foodorderingweb.service.RestaurantService;
import org.sang.foodorderingweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food/")
public class FoodController {

	private final FoodService foodService;
	private final UserService userService;
	private final RestaurantService restaurantService;

	@Autowired
	public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
		this.foodService = foodService;
		this.userService = userService;
		this.restaurantService = restaurantService;
	}

	@GetMapping("/search")
	public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Food> foods = foodService.searchFood(name);
		return new ResponseEntity<>(foods, HttpStatus.OK);
	}

	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<List<Food>> getRestaurantFood(
			@RequestParam boolean vegetarian,
			@RequestParam boolean seasonal,
			@RequestParam boolean nonVeg,
			@RequestParam(required = false) String food_Category,//require false tùy chọn ko bắt buộc trong request http
			@PathVariable Long restaurantId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Food> foods = foodService.getRestaurantsFood(restaurantId, vegetarian, nonVeg, seasonal, food_Category);
		return new ResponseEntity<>(foods, HttpStatus.OK);
	}
}
