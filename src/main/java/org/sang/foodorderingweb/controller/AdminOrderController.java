package org.sang.foodorderingweb.controller;

import java.util.List;
import org.sang.foodorderingweb.model.Order;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.service.OrderService;
import org.sang.foodorderingweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

	private final OrderService orderService;

	private final UserService userService;

	@Autowired
	public AdminOrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("/order/restaurant/{id}")
	public ResponseEntity<List<Order>> getOrderHistory(
			@PathVariable Long id,
			@RequestParam(required = false) String order_status,
			@RequestHeader("Authorization") String jwt
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Order> orders = orderService.getRestaurantsOrder(id, order_status);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@PutMapping("/order/{orderId}/{orderStatus}")
	public ResponseEntity<Order> updateOrderStatus(
			@PathVariable Long orderId,
			@PathVariable String orderStatus,
			@RequestHeader("Authorization") String jwt
	) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Order orders = orderService.updateOrder(orderId, orderStatus);
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
}
