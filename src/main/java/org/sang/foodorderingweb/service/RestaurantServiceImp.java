package org.sang.foodorderingweb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.sang.foodorderingweb.dto.RestaurantDTO;
import org.sang.foodorderingweb.model.Address;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.repository.AddressRepository;
import org.sang.foodorderingweb.repository.RestaurantRepository;
import org.sang.foodorderingweb.repository.UserRepository;
import org.sang.foodorderingweb.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImp implements RestaurantService {

	private final RestaurantRepository restaurantRepo;
	private final AddressRepository addressRepo;
	private final UserRepository userRepo;

	@Autowired
	public RestaurantServiceImp(RestaurantRepository restaurantRepo, AddressRepository addressRepo,
			UserRepository userRepo) {
		this.restaurantRepo = restaurantRepo;
		this.addressRepo = addressRepo;
		this.userRepo = userRepo;
	}

	@Override
	public Restaurant createRestaurant(CreateRestaurantRequest request, User user) {
		Address address = addressRepo.save(request.getAddress());
		Restaurant restaurant = new Restaurant();
		restaurant.setAddress(address);
		restaurant.setContactInformation(request.getContactInformation());
		restaurant.setCuisineType(request.getCuisineType());
		restaurant.setDescription(request.getDescription());
		restaurant.setImages(request.getImages());
		restaurant.setName(request.getName());
		restaurant.setOpeningHours(request.getOpeningHours());
		restaurant.setRegistrationDate(LocalDateTime.now());
		restaurant.setOwner(user);
		return restaurantRepo.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
		Restaurant restaurant = findRestaurantById(restaurantId);
		if (restaurant.getCuisineType() != null) {
			restaurant.setCuisineType(updatedRestaurant.getCuisineType());
		}
		if (restaurant.getDescription() != null) {
			restaurant.setDescription(updatedRestaurant.getDescription());
		}
		if (restaurant.getName() != null) {
			restaurant.setName(updatedRestaurant.getName());
		}
		return restaurantRepo.save(restaurant);
	}

	@Override
	public void deleteRestaurant(Long restaurantId) throws Exception {
		Restaurant restaurant = findRestaurantById(restaurantId);
		restaurantRepo.delete(restaurant);
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepo.findAll();
	}

	@Override
	public List<Restaurant> searchRestaurant(String keyword) {
		return restaurantRepo.findBySearchQuery(keyword);
	}

	@Override
	public Restaurant findRestaurantById(Long restaurantId) throws Exception {
		Optional<Restaurant> opt = restaurantRepo.findById(restaurantId);
		if (opt.isEmpty()) {
			throw new Exception("Restaurant not found with id :" + restaurantId);
		}
		return opt.get();
	}

	@Override
	public Restaurant getRestaurantByUserId(Long userId) throws Exception {
		Restaurant restaurant = restaurantRepo.findByOwnerId(userId);
		if (restaurant == null) {
			throw new Exception("Restaurant not found with owner id: " + userId);
		}
		return restaurant;
	}

	@Override
	public RestaurantDTO addToFavorites(Long restaurantId, User user) throws Exception {
		Restaurant restaurant = findRestaurantById(restaurantId);
		RestaurantDTO dto = new RestaurantDTO();
		dto.setDescription(restaurant.getDescription());
		dto.setImages(restaurant.getImages());
		dto.setTitle(restaurant.getName());
		dto.setId(restaurantId);

		List<RestaurantDTO> favorites = user.getFavorites();
		// Sử dụng phương thức contains để kiểm tra xem nhà hàng đã được thêm vào danh sách yêu thích hay chưa
		boolean isFavorited = favorites.stream().anyMatch(favorite -> favorite.getId().equals(restaurantId));

		if (isFavorited) {
			favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
		} else {
			favorites.add(dto);
		}

		userRepo.save(user);
		return dto;
	}

	@Override
	public Restaurant updateRestaurantStatus(Long id) throws Exception {
		Restaurant restaurant = findRestaurantById(id);
		restaurant.setOpen(!restaurant.isOpen());
		return restaurantRepo.save(restaurant);
	}
}
