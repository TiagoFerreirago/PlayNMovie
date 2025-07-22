package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.mock.GameMock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IgdbClientImplTest {
	
	@Mock private WebClient.Builder webClientBuilder;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;
    
    private IgdbClientImpl igdbClient;
    private GameMock input;

	@BeforeEach
	void setUp() {
        input = new GameMock();

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.headers(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(anyString())).thenAnswer(Invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);

        igdbClient = new IgdbClientImpl(webClientBuilder);
    }

	@SuppressWarnings("unchecked")
	@Test
    void testGetAllGames() {
        GameDto dto = input.gameMockDto(1);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
            .thenReturn(Mono.just(List.of(dto)));

        Flux<GameDto> result = igdbClient.getAllGames(0);

        StepVerifier.create(result)
            .expectNextMatches(g -> g.getId().equals(dto.getId()))
            .verifyComplete();
       
        verify(webClient).post();
        verify(requestBodyUriSpec).uri("/games");
        verify(requestBodyUriSpec).headers(any());
        verify(requestBodyUriSpec).bodyValue(anyString());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).onStatus(any(), any());
        verify(responseSpec).bodyToMono(any(ParameterizedTypeReference.class));
    }

    @SuppressWarnings("unchecked")
	@Test
    void testGetAllGamesEmptyList() {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
            .thenReturn(Mono.just(List.of()));

        Flux<GameDto> result = igdbClient.getAllGames(0);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void testGetAllGamesPageNegative() {
        Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
            igdbClient.getAllGames(-1).blockFirst();
        });
        
        String expectedMessage = "Page number cannot be negative.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
    }

    @SuppressWarnings("unchecked")
	@Test
    void testGetAllGamesApiError() {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
            .thenReturn(Mono.error(new CustomizedBadRequestException("Error querying external API")));

        Flux<GameDto> result = igdbClient.getAllGames(0);

        StepVerifier.create(result).expectError(CustomizedBadRequestException.class).verify();
    }
}
