package org.sang.foodorderingweb.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCartItemRequest {

	private Long foodId;
	private int quantity;
	private List<String> ingredients;
}
