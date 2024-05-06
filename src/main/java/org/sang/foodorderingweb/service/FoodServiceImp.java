package org.sang.foodorderingweb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.sang.foodorderingweb.model.Category;
import org.sang.foodorderingweb.model.Food;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.repository.FoodRepository;
import org.sang.foodorderingweb.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImp implements FoodService{
	private final FoodRepository foodRepo;

	@Autowired
	public FoodServiceImp(FoodRepository foodRepo) {
		this.foodRepo = foodRepo;
	}

	@Override
	public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
		Food food=Food.builder()
				.foodCategory(category)
				.restaurant(restaurant)
				.description(req.getDescription())
				.images(req.getImages())
				.name(req.getName())
				.price(req.getPrice())
				.ingredients(req.getIngredients())
				.isSeasonal(req.isSeasional())
				.isVegetarian(req.isVegetarian())
				.build();
		Food savedFood=foodRepo.save(food);
		restaurant.getFoods().add(savedFood);
		return savedFood;
	}

	@Override
	public void deleteFood(Long foodId) throws Exception {
		Food food=findFoodById(foodId);
		food.setRestaurant(null);
		foodRepo.save(food);

	}

	@Override
	public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal,
			String foodCategory) {
		List<Food>foods=foodRepo.findByRestaurantId(restaurantId);
		if(isVegetarian){
			foods=filterByVegetarian(foods,isVegetarian);
		};
		if(isNonveg){
			foods=filterByNonveg(foods,isNonveg);
		}
		if(isSeasonal){
			foods=filterBySeasonal(foods,isSeasonal);
		}
		if(foodCategory !=null && !foodCategory.isEmpty()){
			foods=filterByCategory(foods,foodCategory);
		}
		return foods;
	}


	private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
		return foods.stream()
				.filter(food -> food.isVegetarian() ==isVegetarian)
				.collect(Collectors.toList());

	}
	private List<Food> filterByNonveg(List<Food> foods, boolean isNonveg) {
		return foods.stream()
				.filter(food -> food.isVegetarian() ==false)
				.collect(Collectors.toList());
	}
	private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
		return foods.stream()
				.filter(food -> food.isVegetarian() ==isSeasonal)
				.collect(Collectors.toList());
	}
	private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
		return foods.stream()
				.filter(food -> {
					if(food.getFoodCategory() != null){
						return food.getFoodCategory().getName().equals(foodCategory);
					}
					return false;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Food> searchFood(String keyword) {
		return foodRepo.searchFood(keyword);
	}

	@Override
	public Food findFoodById(Long foodId) throws Exception {
		Optional<Food> optionalFood=foodRepo.findById(foodId);
		if(optionalFood.isEmpty()){
			throw new Exception("Food not exist ...");
		}
		return optionalFood.get();
	}

	@Override
	public Food updateAvailabilityStatus(Long id) throws Exception {
		Food food=findFoodById(id);
		food.setAvailable(!food.isAvailable());
		return foodRepo.save(food);
	}
}
