package com.th.playnmovie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TmdbConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
