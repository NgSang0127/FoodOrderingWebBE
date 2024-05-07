package org.sang.foodorderingweb.service;

import java.util.List;
import org.sang.foodorderingweb.model.Category;
import org.sang.foodorderingweb.model.Food;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.request.CreateFoodRequest;

public interface FoodService {

	Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

	void deleteFood(Long foodId) throws Exception;

	List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal,
			String foodCategory);

	List<Food> searchFood(String keyword);

	Food findFoodById(Long foodId) throws Exception;

	Food updateAvailabilityStatus(Long id) throws Exception;

}
