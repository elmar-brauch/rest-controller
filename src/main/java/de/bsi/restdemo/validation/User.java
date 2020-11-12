package de.bsi.restdemo.validation;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class User {
	
	@EqualsAndHashCode.Exclude
	private Long id;
	
	@NotNull
	private String name;
	
	@Size(max = 20)
	private String street;
	
	@NotBlank(message = "Special message in case of being not valid")
	private String city;
	
	@Pattern(regexp = "[A-Z]{1,3}")
	private String countryCode;
	
	@Max(99999)
	@Min(0)
	private Integer zipcode;

}
