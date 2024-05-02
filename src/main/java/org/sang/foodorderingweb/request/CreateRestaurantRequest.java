package org.sang.foodorderingweb.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.sang.foodorderingweb.model.Address;
import org.sang.foodorderingweb.model.ContactInformation;

@Data
@Builder
public class CreateRestaurantRequest {
	private Long id;
	private String name;
	private String description;
	private String cuisineType;
	private Address address;
	private ContactInformation contactInformation;
	private String openingHours;
	private List<String> images;
}
