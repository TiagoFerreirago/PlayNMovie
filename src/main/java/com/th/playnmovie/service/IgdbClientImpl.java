package com.th.playnmovie.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IgdbClientImpl implements IgdbClient {

    private static final Logger logger = LoggerFactory.getLogger(IgdbClientImpl.class);
    private static final String GAME_QUERY = 
        "fields id, name, genres, release_dates.date, release_dates.platform, " +
        "release_dates.region, release_dates.id, summary, cover.url; limit 5; offset %d;";

    private final WebClient webClient;

    @Value("${token.key.clientid}")
    private String clientId;

    @Value("${token.key.authorization}")
    private String authorization;

    public IgdbClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.igdb.com/v4/").build();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", clientId);
        headers.set("Authorization", authorization);
        return headers;
    }

    @Override
    public Flux<GameDto> getAllGames(int page) {
        if (page < 0) {
            throw new CustomizedBadRequestException("Page number cannot be negative.");
        }

        int offset = page * 5;
        logger.info("Fetching IGDB games for page {}, offset={}", page, offset);

        return webClient.post()
            .uri("/games")
            .headers(headers -> headers.addAll(getHeaders()))
            .bodyValue(String.format(GAME_QUERY, offset))
            .retrieve()
            .onStatus(status -> status.isError(), response -> {
                logger.error("IGDB API error. Status: {}", response.statusCode());
                return Mono.error(new CustomizedBadRequestException("Error querying external API"));
            })
            .bodyToMono(new ParameterizedTypeReference<List<GameDto>>() {})
            .flatMapMany(Flux::fromIterable);
    }
}

