package com.th.playnmovie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class CustomOpenApi {

	@Bean
	OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("PlayNmovie API")
						.description("API for managing movies, games, favorites, and reviews. Personal project for portfolio.")
						.version("1.0.0")
						.termsOfService("https://github.com/TiagoFerreirago/PlayNMovie")
						.license(new License()
								.name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
	}
}
