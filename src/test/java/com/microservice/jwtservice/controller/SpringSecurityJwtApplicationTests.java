package com.microservice.jwtservice.controller;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.microservice.jwtservice.JwtServiceApplication;
import com.microservice.jwtservice.model.AuthenticationRequest;
import com.microservice.jwtservice.model.AuthenticationResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JwtServiceApplication.class ,webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringSecurityJwtApplicationTests {
	
	@Test
	public void test() {
		 expect()
		  .statusCode(200)
		  .response()
		   .body(containsString("Hello World")).
		 given()
		  .header("jwtToken", "Bearer " + setup().getJwt()).
		 when()
		  .get("/hello");
	}

	private AuthenticationResponse setup() {
		return expect()
			 	.contentType(MediaType.APPLICATION_JSON_VALUE)
			 	.statusCode(200).
		 	 given()
				.body(new AuthenticationRequest("foo", "foo"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.post("/authenticate")
				.as(AuthenticationResponse.class);
	}

}
