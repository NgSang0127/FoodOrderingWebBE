package org.sang.foodorderingweb.service;

import java.util.List;
import org.sang.foodorderingweb.model.IngredientCategory;
import org.sang.foodorderingweb.model.IngredientsItem;

public interface IngredientsService {

	IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

	IngredientCategory findIngredientCategory(Long id) throws Exception;

	List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception;

	IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception;

	List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);

	IngredientsItem updateStock(Long id) throws Exception;


}
