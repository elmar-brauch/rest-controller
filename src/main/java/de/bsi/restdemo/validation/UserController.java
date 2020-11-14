package de.bsi.restdemo.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private List<User> users = new ArrayList<>();
	
	@PostMapping
	public ResponseEntity<Long> addUser(@Valid @RequestBody User user) {
		user.setId(UUID.randomUUID().getMostSignificantBits());
		users.add(user);
		return ResponseEntity.ok(user.getId());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUsers(@PathVariable long id) {
		if (1 == id)
			throw new NoSuchElementException("User id 1 triggers general ExceptionHandler");
		var result = users.stream().filter(user -> id == user.getId()).findFirst().orElseThrow();
		return ResponseEntity.ok(result);
	}
	
	/*
	 * old-school example shows the way without annotation based validation.
	 */
	@PostMapping("/old-school")
	public ResponseEntity<Long> createUser(@RequestBody User user) {
		String notValidatedName = user.getName();
		String regexForName = "[A-Z a-z]{1,20}";
		String validationErrorForName = "User's name %s does not match regualr expression %s"; 
		if (notValidatedName == null || !notValidatedName.matches(regexForName))
			throw new IllegalArgumentException(String.format(
					validationErrorForName, notValidatedName, regexForName));
		// Validation of other attributes is skipped here...
		user.setId(UUID.randomUUID().getMostSignificantBits());
		users.add(user);
		return ResponseEntity.ok(user.getId());
	}

}
