package de.bsi.restdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/item")
public class ItemController {

	List<Item> items = new ArrayList<>();
	
	@PostMapping
	public ResponseEntity<Void> apendItem(@RequestBody Item newItem) {
		items.add(newItem);
		// HTTP Code 201 wird zurueck geschickt, 
		// weil ein neues Item zur Liste hinzugefuegt wurde.
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<Object> getItemByName(@RequestParam(required = false) String itemName) {
		// Mit der Java 8 Stream API wird durch die Liste aller Items iteriert
		// und das erste Item mit gleichem Namen wird zurückgeschickt.
		Optional<Item> optItem = items.stream()
				.filter(item -> itemName != null && itemName.equalsIgnoreCase(item.getName()))
				.findFirst();
		if (optItem.isPresent())
			return ResponseEntity.ok(optItem.get());
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping(path = "/{itemId}")
	public ResponseEntity<Void> deleteItemById(@PathVariable String itemId) {
		// In Java 8 wurde das Collections Interface um Lambda unterstützende Methoden erweitert.
		// Hier werden alle Item Instanzen entfernt für die der Lambda Ausdruck true liefert.
		if (items.removeIf(item -> itemId != null && itemId.equals(item.getId())))
			return ResponseEntity.noContent().build();
		return ResponseEntity.notFound().build();
	}
	
}
