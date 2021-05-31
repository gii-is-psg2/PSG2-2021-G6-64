package org.springframework.samples.petclinic.model;

import javax.persistence.Column;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class Contact{

	@Column(name = "name")
	@NotBlank
	private String name;

	@Column(name = "email")
	@NotBlank
	private String email;

	@Column(name = "phone")
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