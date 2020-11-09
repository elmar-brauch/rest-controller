package de.bsi.restdemo.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;

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

@SpringBootTest(classes = { UserController.class, RestResponseEntityExceptionHandler.class } )
@AutoConfigureMockMvc
@EnableWebMvc
class UserControllerTest {

	@Autowired UserController controller;
	@Autowired MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private static final String USER_PATH = "/user";
	
	@Test
	void positiveTest() throws Exception {
		var user = createUser();
		
		var postResponse = sendPost(user);
		assertEquals(200, postResponse.getStatus());
		Long userId = mapper.readValue(postResponse.getContentAsString(), Long.class);
		
		RequestBuilder getRequest = MockMvcRequestBuilders
				.get(USER_PATH + "/" + userId);
		MockHttpServletResponse getResponse = mockMvc.perform(getRequest).andReturn().getResponse();
		assertEquals(200, getResponse.getStatus());
		assertEquals(user, mapper.readValue(getResponse.getContentAsString(), User.class));
	}
	
	@Test
	void validateMax() throws Exception {
		var user = createUser();
		user.setZipcode(100000);
		var postResponse = sendPost(user);
		assertEquals(400, postResponse.getStatus());
		assertValidationFailedFor(postResponse, "zipcode");
	}
	
	@Test
	void validateRegEx() throws Exception {
		var user = createUser();
		user.setCountryCode("de");
		assertValidationFailedFor(sendPost(user), "countryCode");
	}
	
	@Test
	void validateSize() throws Exception {
		var user = createUser();
		user.setStreet("Strassenname ist länger als @Size erlaubt.");
		assertValidationFailedFor(sendPost(user), "street");
	}
	
	@Test
	void validateNotEmpty() throws Exception {
		var user = createUser();
		user.setCity("");
		assertValidationFailedFor(sendPost(user), "city");
		assertValidationFailedFor(sendPost(user), "Special message in case of being not valid");
	}
	
	@Test
	void validateNotNull() throws Exception {
		var user = createUser();
		user.setName(null);
		assertValidationFailedFor(sendPost(user), "name");
	}
	
	@Test
	void unknownExceptioHandlerTest() throws Exception {
		RequestBuilder getRequest = MockMvcRequestBuilders
				.get(USER_PATH + "/1");
		MockHttpServletResponse getResponse = mockMvc.perform(getRequest).andReturn().getResponse();
		assertEquals(500, getResponse.getStatus());
		assertTrue(getResponse.getContentAsString().contains("Unknown & unexpected exception poped up."));
	}
	
	private void assertValidationFailedFor(MockHttpServletResponse response, String invalidAttributeName)
			throws UnsupportedEncodingException {
		var errorMsg = response.getContentAsString();
		assertTrue(errorMsg.contains(invalidAttributeName) && errorMsg.contains("Validation failed"));
	}
	
	private MockHttpServletResponse sendPost(User user) throws Exception {
		RequestBuilder postRequest = MockMvcRequestBuilders
				.post(USER_PATH)
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON);
		return mockMvc.perform(postRequest).andReturn().getResponse();
	}
	
	private User createUser() {
		User user = new User();
		user.setName("Elmar Brauch");
		user.setZipcode(64295);
		user.setStreet("Bessunger Str. 82");
		user.setCity("Darmstadt");
		user.setCountryCode("DE");
		return user;
	}

}
