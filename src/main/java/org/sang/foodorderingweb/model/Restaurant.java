package org.sang.foodorderingweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private User owner;

	private String name;

	private String description;

	private String cuisineType;//loại ẩm thực

	@OneToOne
	private Address address;

	@Embedded
	private ContactInformation contactInformation;

	private String openingHours;

	@OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
	//orphanRemoval =true nghĩa là khi tôi xóa một Order ra khỏi danh sách orders của một restaurant
	//order đó sẽ bị xóa khỏi cơ sơ dữ liệu
	private List<Order> orders=new ArrayList<>();

	@ElementCollection
	@Column(length = 1000)
	private List<String> images;

	private LocalDateTime registrationDate;

	private boolean open;

	@JsonIgnore
	@OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL)
	private List<Food> foods=new ArrayList<>();
}
