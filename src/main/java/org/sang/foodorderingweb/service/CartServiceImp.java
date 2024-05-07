package org.sang.foodorderingweb.service;

import java.util.Optional;
import org.sang.foodorderingweb.model.Cart;
import org.sang.foodorderingweb.model.CartItem;
import org.sang.foodorderingweb.model.Food;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.repository.CartItemRepository;
import org.sang.foodorderingweb.repository.CartRepository;
import org.sang.foodorderingweb.repository.FoodRepository;
import org.sang.foodorderingweb.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService{

	private final CartRepository cartRepo;
	private final UserService userService;
	private final CartItemRepository cartItemRepo;
	private final FoodService foodService;

	@Autowired
	public CartServiceImp(CartRepository cartRepo, UserService userService, CartItemRepository cartItemRepo,
			FoodService foodService) {
		this.cartRepo = cartRepo;
		this.userService = userService;
		this.cartItemRepo = cartItemRepo;
		this.foodService = foodService;
	}




	@Override
	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Food food=foodService.findFoodById(req.getFoodId());
		Cart cart=cartRepo.findByCustomerId(user.getId());

		for(CartItem cartItem : cart.getItems()){
			if(cartItem.getFood().equals(food)){
				int newQuantity=cartItem.getQuantity() + req.getQuantity();
				return updateCartItemQuantity(cartItem.getId(),newQuantity);
			}
		}
		CartItem newItems=CartItem.builder()
				.food(food)
				.cart(cart)
				.quantity(req.getQuantity())
				.ingredients(req.getIngredients())
				.totalPrice(req.getQuantity()*food.getPrice())
				.build();

		CartItem savedCartItem=cartItemRepo.save(newItems);
		cart.getItems().add(savedCartItem);

		return savedCartItem;
	}

	@Override
	public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
		Optional<CartItem> cartItemOptional=cartItemRepo.findById(cartItemId);
		if(cartItemOptional.isEmpty()){
			throw new Exception("Cart item not found");
		}
		CartItem item=cartItemOptional.get();
		item.setQuantity(quantity);
		item.setTotalPrice(item.getFood().getPrice() * quantity);

		return cartItemRepo.save(item);
	}

	@Override
	public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Cart cart=cartRepo.findByCustomerId(user.getId());
		Optional<CartItem> cartItemOptional=cartItemRepo.findById(cartItemId);
		if(cartItemOptional.isEmpty()){
			throw new Exception("Cart item not found");
		}
		CartItem item=cartItemOptional.get();
		cart.getItems().remove(item);
		return cartRepo.save(cart);
	}

	@Override
	public Long calculateCartTotals(Cart cart) throws Exception {
		Long total =0L;
		for(CartItem item : cart.getItems()){
			total +=item.getFood().getPrice() * item.getQuantity();
		}
		return total;
	}

	@Override
	public Cart findCartById(Long id) throws Exception {
		Optional<Cart> cartOptional=cartRepo.findById(id);
		if(cartOptional.isEmpty()){
			throw new Exception("Cart not found with id:"+id);
		}
		return cartOptional.get();
	}

	@Override
	public Cart findCartByUserId(String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		return cartRepo.findByCustomerId(user.getId());
	}

	@Override
	public Cart cleanCart(String jwt) throws Exception {
		Cart cart=findCartByUserId(jwt);
		cart.getItems().clear();
		return cartRepo.save(cart);
	}
}
