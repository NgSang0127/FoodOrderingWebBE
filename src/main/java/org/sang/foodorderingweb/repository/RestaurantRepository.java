package org.sang.foodorderingweb.repository;

import java.util.List;
import org.sang.foodorderingweb.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	@Query("SELECT r FROM Restaurant r WHERE lower (r.name) LIKE lower(concat('%s',:query,'%s') ) OR lower (r.cuisineType) LIKE"
			+ " lower(concat('%s',:query,'%s') )")
	List<Restaurant> findBySearchQuery(String query);

	Restaurant findByOwnerId(Long userId);

}
