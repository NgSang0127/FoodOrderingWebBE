package org.sang.foodorderingweb.service;

import com.stripe.exception.StripeException;
import org.sang.foodorderingweb.model.Order;
import org.sang.foodorderingweb.response.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentService {
	PaymentResponse createPaymentLink(Order order) throws StripeException;

}
