package org.sang.foodorderingweb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.sang.foodorderingweb.model.Address;
import org.sang.foodorderingweb.model.Cart;
import org.sang.foodorderingweb.model.CartItem;
import org.sang.foodorderingweb.model.Order;
import org.sang.foodorderingweb.model.OrderItem;
import org.sang.foodorderingweb.model.Restaurant;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.repository.AddressRepository;
import org.sang.foodorderingweb.repository.OrderItemRepository;
import org.sang.foodorderingweb.repository.OrderRepository;
import org.sang.foodorderingweb.repository.UserRepository;
import org.sang.foodorderingweb.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp implements OrderService {

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final AddressRepository addressRepo;
	private final UserRepository userRepo;
	private final RestaurantService restaurantService;
	private final CartService cartService;

	@Autowired
	public OrderServiceImp(OrderRepository orderRepo, OrderItemRepository orderItemRepo, AddressRepository addressRepo
			, UserRepository userRepo, RestaurantService restaurantService, CartService cartService) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.addressRepo = addressRepo;
		this.userRepo = userRepo;
		this.restaurantService = restaurantService;
		this.cartService = cartService;
	}

	@Override
	public Order createOrder(OrderRequest order, User user) throws Exception {
		Address shippAddress = order.getDelivery();
		Address savedAddress = addressRepo.save(shippAddress);
		if (!user.getAddresses().contains(savedAddress)) {
			user.getAddresses().add(savedAddress);
			userRepo.save(user);
		}
		Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());
		Order createOrder = new Order();
		createOrder.setCustomer(user);
		createOrder.setCreatedAt(new Date());
		createOrder.setOrderStatus("PENDING");
		createOrder.setDeliveryAddress(savedAddress);
		createOrder.setRestaurant(restaurant);

		Cart cart = cartService.findCartByUserId(user.getId());
		List<OrderItem> ordersItems = new ArrayList<>();
		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setFood(cartItem.getFood());
			orderItem.setIngredients(cartItem.getIngredients());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());

			OrderItem savedOrderItem = orderItemRepo.save(orderItem);
			ordersItems.add(savedOrderItem);
		}
		Long totalPrice = cartService.calculateCartTotals(cart);
		createOrder.setItems(ordersItems);
		createOrder.setTotalPrice(totalPrice);

		Order savedOrder = orderRepo.save(createOrder);
		restaurant.getOrders().add(savedOrder);
		return createOrder;
	}

	@Override
	public Order updateOrder(Long orderId, String orderStatus) throws Exception {
		Order order = findOrderById(orderId);
		if (orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED") || orderStatus.equals(
				"COMPLETED") || orderStatus.equals("PENDING")) {
			order.setOrderStatus(orderStatus);
			return orderRepo.save(order);
		}
		throw new Exception("Please select a valid order status");
	}

	@Override
	public void cancelOrder(Long orderId) throws Exception {
		Order order = findOrderById(orderId);
		orderRepo.deleteById(orderId);
	}

	@Override
	public List<Order> getUsersOrder(Long userId) throws Exception {
		return orderRepo.findByCustomerId(userId);
	}

	@Override
	public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
		List<Order> orders = orderRepo.findByRestaurantId(restaurantId);
		if (orderStatus != null) {
			orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus))
					.collect(Collectors.toList());
		}
		return orders;
	}

	@Override
	public Order findOrderById(Long orderId) throws Exception {
		Optional<Order> optionalOrder = orderRepo.findById(orderId);
		if (optionalOrder.isEmpty()) {
			throw new Exception("Order not found");
		}

		return optionalOrder.get();
	}
}
