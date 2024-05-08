package org.sang.foodorderingweb.service;

import java.util.List;
import org.sang.foodorderingweb.model.Order;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.request.OrderRequest;

public interface OrderService {

	Order createOrder(OrderRequest order, User user) throws Exception;

	Order updateOrder(Long orderId, String orderStatus) throws Exception;

	void cancelOrder(Long orderId) throws Exception;

	List<Order> getUsersOrder(Long userId) throws Exception;

	List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;

	Order findOrderById(Long orderId) throws Exception;
}
