package org.sang.foodorderingweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sang.foodorderingweb.dto.RestaurantDTO;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String fullName;

	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<Order> orders = new ArrayList<>();

	@ElementCollection
	private List<RestaurantDTO> favorites = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)//when user delete ,all address will be delete
	private List<Address> addresses = new ArrayList<>();
}
