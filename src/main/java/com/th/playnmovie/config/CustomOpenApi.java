package com.th.playnmovie.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

public class CustomOpenApi {

	OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title(null)
						.description(null)
						.version(null)
						.termsOfService(null)
						.license(new License()
								.name(null)
								.url(null)));
	}
}
