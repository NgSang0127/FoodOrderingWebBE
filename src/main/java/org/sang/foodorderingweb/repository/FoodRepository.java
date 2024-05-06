package org.sang.foodorderingweb.repository;

import java.util.List;
import org.sang.foodorderingweb.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepository extends JpaRepository<Food,Long> {
	List<Food> findByRestaurantId(Long restaurantId);

	@Query("SELECT f From Food f WHERE f.name LIKE %:keyword% OR f.foodCategory.name LIKE %:keyword%")
	List<Food> searchFood(@Param("keyword") String keyword);
}
