package org.sang.foodorderingweb.service;

import java.util.List;
import org.sang.foodorderingweb.dto.RestaurantDTO;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.request.CreateRestaurantRequest;



public interface RestaurantService {
	Restaurant createRestaurant(CreateRestaurantRequest request, User user);
	Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updatedRestaurant) throws Exception;
	void deleteRestaurant(Long restaurantId) throws Exception;
	List<Restaurant> getAllRestaurants();
	List<Restaurant> searchRestaurant(String keyword);
	Restaurant findRestaurantById(Long restaurantId) throws Exception;//in ra ngoại lệ nếu không có gì được tìm thấy
	Restaurant getRestaurantByUserId(Long userId) throws Exception;
	RestaurantDTO addToFavorites(Long restaurantId,User user) throws Exception;
	Restaurant updateRestaurantStatus(Long id)throws Exception;

}
