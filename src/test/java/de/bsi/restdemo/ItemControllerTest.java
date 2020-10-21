package de.bsi.restdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ItemController itemController;
	
	private Item ballItem = new Item(), schuhItem = new Item();
	
	private static final String ITEM_PATH = "/item";
	
	@BeforeEach
	void setup() {
		ballItem.setId("123");
		ballItem.setName("Ball");
		schuhItem.setId("456");
		schuhItem.setName("Schuh");
		itemController.items = new ArrayList<>();
	}
	
	@Test
	void testPost() throws Exception {
		RequestBuilder postRequest = MockMvcRequestBuilders
				.post(ITEM_PATH)
				.content(asJsonString(ballItem))
				.contentType(MediaType.APPLICATION_JSON);
		MockHttpServletResponse response = mockMvc.perform(postRequest).andReturn().getResponse();
		assertEquals(201, response.getStatus());
		assertEquals(1, itemController.items.size());
	}
	
	@Test
	void testGet() throws Exception {
		itemController.items = List.of(schuhItem, ballItem);
		RequestBuilder getRequest = MockMvcRequestBuilders
				.get(ITEM_PATH + "?itemName=ball");
		mockMvc.perform(getRequest)
				.andExpect(jsonPath("$.name").value("Ball"))
				.andExpect(jsonPath("$.id").value("123"));
	}
	
	@Test
	void testDelete() throws Exception {
		itemController.items.addAll(List.of(ballItem, schuhItem, ballItem));
		assertEquals(3, itemController.items.size());
		RequestBuilder deleteRequest = MockMvcRequestBuilders
				.delete(ITEM_PATH + "/123");
		mockMvc.perform(deleteRequest);
		assertEquals(1, itemController.items.size());
	}
	
	private static String asJsonString(final Object obj) throws Exception {
		return new ObjectMapper().writeValueAsString(obj);
	}

}
