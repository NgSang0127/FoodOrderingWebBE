package org.sang.foodorderingweb.request;

import java.util.List;
import lombok.Data;
import org.sang.foodorderingweb.model.Category;
import org.sang.foodorderingweb.model.IngredientsItem;

@Data
public class CreateFoodRequest {

	private String name;

	private String description;

	private Long price;

	private Category category;

	private List<String> images;

	private Long restaurantId;

	private boolean vegetarian;

	private boolean seasonal;

	private List<IngredientsItem> ingredients;

}
