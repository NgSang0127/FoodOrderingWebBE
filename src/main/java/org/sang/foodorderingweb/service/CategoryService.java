package org.sang.foodorderingweb.service;

import java.util.List;
import org.sang.foodorderingweb.model.Category;

public interface CategoryService {
	Category createCategory(String name,Long userId) throws Exception;

	List<Category> findCategoryByRestaurantId(Long id) throws Exception;

	Category findCategoryById(Long id) throws Exception;


}
