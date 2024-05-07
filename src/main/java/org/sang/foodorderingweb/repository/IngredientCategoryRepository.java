package org.sang.foodorderingweb.repository;

import java.util.List;
import org.sang.foodorderingweb.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory,Long> {

	List<IngredientCategory> findByRestaurantId(Long id);

}
