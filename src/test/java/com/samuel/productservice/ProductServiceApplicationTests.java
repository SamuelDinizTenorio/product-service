package com.samuel.productservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.samuel.productservice.infrastructure.config.BaseIntegrationTest;

@DisplayName("Integration: Application Context")
class ProductServiceApplicationTests extends BaseIntegrationTest {

	@Test
	@DisplayName("Should load application context and MySQL container start up successfully")
	void contextLoads() {
	}

}
