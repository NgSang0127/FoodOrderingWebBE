package org.sang.foodorderingweb.repository;

import java.util.List;
import org.sang.foodorderingweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

	List<Category>findByRestaurantId(Long id);

}
