package org.sang.foodorderingweb.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientCategoryRequest {
	private String name;
	private Long restaurantId;
}
