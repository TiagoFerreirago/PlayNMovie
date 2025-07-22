package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.mock.GameMock;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.model.GenreEnum;
import com.th.playnmovie.repository.GameRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class IgdbServiceTest {
	
	 @Mock 
	 private GameRepository gameRepository;
	
	 @Mock
	 private IgdbClient igdbClient;

	 @InjectMocks
	 private IgdbService igdbService;

	 private GameMock input;
	
	@BeforeEach
	void setUp() throws Exception {
		
		input = new GameMock();
	}
	
	
	@Test
	void testFindByNameOrGenresDb() {
		Game game = input.gameMock(1);
		game.setId(1L);
		
		when(gameRepository.findGames(any(), any())).thenReturn(List.of(game));
		
		var result = igdbService.findByNameOrGenres("Test Game", List.of(GenreEnum.RPG.getName()));
		
		StepVerifier.create(result)
		.expectNextMatches(dto -> dto.getId().equals(game.getId()))
		.verifyComplete();
		verify(gameRepository).findGames(any(), any());
				
	}
	
	@Test
	void testFindByNameOrGenresApi() {
		when(gameRepository.findGames(any(), any())).thenReturn(List.of());
		
		GameDto dto = input.gameMockDto(1);
		dto.setGenresFromIds(List.of(12));
		dto.setGenreIds(List.of(12));
		
		when(igdbClient.getAllGames(anyInt())).thenReturn(Flux.just(dto));
		
		Flux<GameDto> result = igdbService.findByNameOrGenres("Test Game", List.of(GenreEnum.RPG.getName()));
		
		StepVerifier.create(result)
		.expectNextMatches(games -> games.getGenres().contains(GenreEnum.RPG.getName()))
		.verifyComplete();
		verify(igdbClient, times(10)).getAllGames(anyInt());
	}
	
	@Test
	void testFindByNameOrGenresApiNoMatches() {
	    when(gameRepository.findGames(any(), any())).thenReturn(List.of());

	    GameDto dto = input.gameMockDto(1);
	    dto.setGenresFromIds(List.of(99)); 
	    dto.setGenreIds(List.of(99));
	    dto.setTitle("Other game");

	    when(igdbClient.getAllGames(anyInt())).thenReturn(Flux.just(dto));

	    Flux<GameDto> result = igdbService.findByNameOrGenres("Test Game", List.of(GenreEnum.RPG.getName()));

	    StepVerifier.create(result).verifyComplete();
	}

	@Test
	void testFindByNameOrGenresInvalidParams() {
		Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
			igdbService.findByNameOrGenres(null, null).blockFirst();
		});
		
		 String expectedMessage = "Please enter at least one name or genre to search.";
		 String messageReleased = exception.getMessage();
		    
		 assertEquals(messageReleased, expectedMessage);
	}
	
	
	@Test
	void testFindByNameOrGenres_GenreEmpty() {
		List<String> genres = List.of(GenreEnum.RPG.getName(),"");
		
		Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
			igdbService.findByNameOrGenres(null, genres).blockFirst();
		});
		
		String expectedMessage = "Invalid genre: empty or null value.";
		String messageReleased = exception.getMessage();
		    
		assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testFindByNameOrGenres_GenreInvalid() {
		 List<String> genres = List.of("FakeGenre");
		 
		 Exception exception = assertThrows(CustomizedBadRequestException.class, () -> {
			 igdbService.findByNameOrGenres(null, genres).blockFirst();
		 });
		 
		 String expectedMessage = "Invalid genre: FakeGenre";
		 String messageReleased = exception.getMessage();
		    
		 assertEquals(messageReleased, expectedMessage);
	}
}
