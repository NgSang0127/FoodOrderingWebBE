package org.sang.foodorderingweb.service;

import java.util.List;
import java.util.Optional;
import org.sang.foodorderingweb.model.IngredientCategory;
import org.sang.foodorderingweb.model.IngredientsItem;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.repository.IngredientCategoryRepository;
import org.sang.foodorderingweb.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImp implements IngredientsService{

	private final IngredientCategoryRepository ingredientCategoryRepo;
	private final IngredientItemRepository ingredientItemRepo;
	private final RestaurantService restaurantService;

	@Autowired
	public IngredientServiceImp(IngredientCategoryRepository ingredientCategoryRepo,
			IngredientItemRepository ingredientItemRepo, RestaurantService restaurantService) {
		this.ingredientCategoryRepo = ingredientCategoryRepo;
		this.ingredientItemRepo = ingredientItemRepo;
		this.restaurantService = restaurantService;
	}




	@Override
	public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=IngredientCategory.builder()
				.restaurant(restaurant)
				.name(name)
				.build();
		return ingredientCategoryRepo.save(category);
	}

	@Override
	public IngredientCategory findIngredientCategory(Long id) throws Exception {
		Optional<IngredientCategory> optionalCategory=ingredientCategoryRepo.findById(id);
		if(optionalCategory.isEmpty()){
			throw new Exception("Ingredient category not found ...");
		}
		return optionalCategory.get();
	}

	@Override
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
		restaurantService.findRestaurantById(id);
		return ingredientCategoryRepo.findByRestaurantId(id);
	}

	@Override
	public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId)
			throws Exception {
		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		IngredientCategory category=findIngredientCategory(categoryId);
		IngredientsItem item=IngredientsItem.builder()
				.name(ingredientName)
				.restaurant(restaurant)
				.category(category)
				.build();
		IngredientsItem ingredientsItem=ingredientItemRepo.save(item);
		category.getIngredients().add(ingredientsItem);
		return ingredientsItem;
	}

	@Override
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
		return ingredientItemRepo.findByRestaurantId(restaurantId);
	}

	@Override
	public IngredientsItem updateStock(Long id) throws Exception {
		Optional<IngredientsItem> optionalIngredientsItem=ingredientItemRepo.findById(id);
		if(optionalIngredientsItem.isEmpty()){
			throw new Exception("Ingredient not found ...");
		}
		IngredientsItem ingredientsItem=optionalIngredientsItem.get();
		ingredientsItem.setStoke(!ingredientsItem.isStoke());
		return ingredientItemRepo.save(ingredientsItem);
	}
}
