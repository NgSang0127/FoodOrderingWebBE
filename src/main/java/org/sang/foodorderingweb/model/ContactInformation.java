package org.sang.foodorderingweb.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactInformation {

	private String email;

	private String phone;

	private String twitter;

	private String tiktok;

	private String facebook;

	private String instagram;

}
