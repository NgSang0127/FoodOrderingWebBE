package org.sang.foodorderingweb.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
public class RestaurantDTO {
	private String title;

	@Column(length = 1000)
	private List<String> images;

	private String description;
	private Long id;
}
