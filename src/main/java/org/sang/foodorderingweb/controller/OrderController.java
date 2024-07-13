package org.sang.foodorderingweb.controller;

import java.util.List;
import org.sang.foodorderingweb.model.Order;
import org.sang.foodorderingweb.model.User;
import org.sang.foodorderingweb.request.OrderRequest;
import org.sang.foodorderingweb.response.PaymentResponse;
import org.sang.foodorderingweb.service.OrderService;
import org.sang.foodorderingweb.service.PaymentService;
import org.sang.foodorderingweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

	private final OrderService orderService;

	private final UserService userService;

	private final PaymentService paymentService;

	@Autowired
	public OrderController(OrderService orderService, UserService userService,PaymentService paymentService) {
		this.orderService = orderService;
		this.userService = userService;
		this.paymentService = paymentService;
	}

	@PostMapping("/order")
	public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Order order = orderService.createOrder(req, user);
		PaymentResponse res=paymentService.createPaymentLink(order);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/order/user")
	public ResponseEntity<List<Order>> getOrderHistory(
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		List<Order> orders = orderService.getUsersOrder(user.getId());
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
}
