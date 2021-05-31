package org.springframework.samples.petclinic.model;

import javax.persistence.Column;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class Contact{

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private String phone;

	public Contact(@NotBlank String name, @NotBlank String email, @NotBlank String phone) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return "Contact [name=" + name + ", email=" + email + ", phone=" + phone + "]";
	}

}
