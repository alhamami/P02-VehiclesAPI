package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.springframework.http.HttpStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;


	private final TestRestTemplate restTemplate;


	public PricingServiceApplicationTests(TestRestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPrice() {

		String url = "http://localhost:" + port + "/pricing/1";
		ResponseEntity<Price> response = this.restTemplate.getForEntity(url, Price.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

	}

}
