package org.sang.foodorderingweb.service;

import java.util.List;
import java.util.Optional;
import org.sang.foodorderingweb.model.Category;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp implements CategoryService {

	private final RestaurantService restaurantService;
	private final CategoryRepository categoryRepo;

	@Autowired
	public CategoryServiceImp(RestaurantService restaurantService, CategoryRepository categoryRepo) {
		this.restaurantService = restaurantService;
		this.categoryRepo = categoryRepo;
	}

	@Override
	public Category createCategory(String name, Long userId) throws Exception {
		Restaurant restaurant = restaurantService.findRestaurantById(userId);

		Category category=new Category();
		category.setName(name);
		category.setRestaurant(restaurant);
		return categoryRepo.save(category);
	}

	@Override
	public List<Category> findCategoryByRestaurantId(Long id) throws Exception {

		return categoryRepo.findByRestaurantId(id);
	}

	@Override
	public Category findCategoryById(Long id) throws Exception {
		Optional<Category> optionalCategory = categoryRepo.findById(id);
		if (optionalCategory.isEmpty()) {
			throw new Exception("Category not found");
		}
		return optionalCategory.get();
	}
}
