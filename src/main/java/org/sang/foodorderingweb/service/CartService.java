package org.sang.foodorderingweb.service;

import org.sang.foodorderingweb.model.Cart;
import org.sang.foodorderingweb.model.CartItem;
import org.sang.foodorderingweb.request.AddCartItemRequest;

public interface CartService {

	CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception;

	CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

	Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;

	Long calculateCartTotals(Cart cart) throws Exception;

	Cart findCartById(Long id) throws Exception;

	Cart findCartByUserId(String jwt) throws Exception;

	Cart cleanCart(String jwt) throws Exception;
}
