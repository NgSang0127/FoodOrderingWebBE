package org.sang.foodorderingweb.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientItemRequest {

	private String name;
	private Long categoryId;
	private Long restaurantId;
}
