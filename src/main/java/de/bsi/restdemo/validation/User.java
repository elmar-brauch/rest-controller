package de.bsi.restdemo.validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	
	@NotEmpty(message = "Special message in case of being not valid")
	private String city;
	
	@Pattern(regexp = "[A-Z]{1,3}")
	private String countryCode;
	
	@Max(99999)
	@Min(0)
	private Integer zipcode;

}
