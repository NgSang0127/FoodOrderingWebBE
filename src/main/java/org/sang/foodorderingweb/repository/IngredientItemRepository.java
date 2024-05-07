package org.sang.foodorderingweb.repository;

import java.util.List;
import org.sang.foodorderingweb.model.IngredientsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientItemRepository  extends JpaRepository<IngredientsItem,Long> {
	List<IngredientsItem> findByRestaurantId(Long id);

}
