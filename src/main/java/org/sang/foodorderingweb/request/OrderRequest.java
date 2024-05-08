package org.sang.foodorderingweb.request;

import lombok.Builder;
import lombok.Data;
import org.sang.foodorderingweb.model.Address;

@Data
@Builder
public class OrderRequest {
	private Long restaurantId;
	private Address delivery;
}
