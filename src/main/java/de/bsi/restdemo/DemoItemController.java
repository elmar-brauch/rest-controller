package de.bsi.restdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO Translate this class in live demos into ItemController
public class DemoItemController {

	List<Item> items = new ArrayList<>();
	
	public void apendItem(Item newItem) {
		items.add(newItem);
	}
	
	public Object getItemByName(String itemName) {
		// Mit der Java 8 Stream API wird durch die Liste aller Items iteriert
		// und das erste Item mit gleichem Namen wird zurückgeschickt.
		Optional<Item> optItem = items.stream()
				.filter(item -> itemName != null && itemName.equalsIgnoreCase(item.getName()))
				.findFirst();
		if (optItem.isPresent())
			return optItem.get();
		return null;
	}
	
	public void deleteItemById(String itemId) {
		items.removeIf(item -> itemId != null && itemId.equals(item.getId()));
	}
	
}
