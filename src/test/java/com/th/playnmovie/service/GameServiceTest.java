package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.GameNotFoundException;
import com.th.playnmovie.mapper.GameMapper;
import com.th.playnmovie.mock.GameMock;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.repository.GameRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class GameServiceTest {

	private GameMock input;
	@InjectMocks
	private GameService gameService;
	@Mock
	private IgdbService igdbService;
	@Mock
	private IgdbClient igdbClient;
	@Mock
	private GameRepository gameRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		input = new GameMock();
	}

	@Test
	void testCreateGame() {
		
		Game gamePersistence = input.gameMock(1);
		gamePersistence.setId(1L);
		
		GameDto dto = input.gameMockDto();
		dto.setId(1L);
		
		when(gameRepository.save(any(Game.class))).thenReturn(gamePersistence);
		
		var result = gameService.createGame(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(result.getReleaseDate(), LocalDate.of(2025, 6, 24));
		assertEquals(result.getSynopsis(),"synopsis dto 1");
		assertEquals(result.getTitle(), "title dto 1");
		assertEquals(result.getUrl(), "url dto 1");
		
		verify(gameRepository).save(any(Game.class));
	}

	@Test
	void testUpdateGame() {
		Game game = input.gameMock();
		game.setId(1L);
		Game gamePersistence = game;
		gamePersistence.setId(1L);
		
		GameDto dto = input.gameMockDto();
		dto.setId(1L);	
		
		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		when(gameRepository.save(game)).thenReturn(gamePersistence);
		
		var result = gameService.updateGame(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(result.getReleaseDate(), LocalDate.of(2025, 6, 24));
		assertEquals(result.getSynopsis(),"synopsis dto 1");
		assertEquals(result.getTitle(), "title dto 1");
		assertEquals(result.getUrl(), "url dto 1");
		verify(gameRepository).findById(1L);
		verify(gameRepository).save(game);
	}

	@Test
	void testDeleteGame() {

		Game game = input.gameMock();
		game.setId(1L);
		
		when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
		
		gameService.deleteGame(1L);
		
		verify(gameRepository).findById(1L);

	}

	@Test
	void testGetGamesFromDb() {
		Game game = input.gameMock();
		game.setId(1L);
		Game gameTwo = input.gameMock();
		gameTwo.setId(2L);
			
		GameDto dto = GameMapper.toDto(game);
		GameDto dtoTwo = GameMapper.toDto(gameTwo);	
		
		
        when(gameRepository.findAll()).thenReturn(List.of(game, gameTwo));

        Flux<GameDto> result = gameService.getGamesFromDb();
        
        StepVerifier.create(result)
        .expectNext(dto)
        .expectNext(dtoTwo)
        .verifyComplete();	
        
        verify(gameRepository).findAll();
	}

	@Test
	void testGetGames() {
		Game game = input.gameMock();
		game.setId(1L);
		Game gameTwo = input.gameMock();
		gameTwo.setId(2L);
		
		GameDto dto = GameMapper.toDto(game);
		GameDto dtoTwo = GameMapper.toDto(gameTwo);	
		
		GameDto apiResult = input.gameMockDto();
		apiResult.setId(3L);
		
		when(gameRepository.findAll()).thenReturn(List.of(game,gameTwo));
		when(igdbClient.getAllGames(1)).thenReturn(Flux.just(apiResult));
		
		var result = gameService.getGamesFromDbAndApi(1);
		
		StepVerifier.create(result)
		.expectNext(dto)
		.expectNext(dtoTwo)
		.expectNext(apiResult)
		.verifyComplete();
		
		verify(gameRepository).findAll();
		verify(igdbClient).getAllGames(anyInt());
				
	}
	
	@Test
	void testCreateGameNullDto() {
	    
		Exception exception = assertThrows(CustomizedBadRequestException.class, () -> gameService.createGame(null));
	    
	    String expectedMessage = "Game data not provided.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testCreateGameMissingTitle() {
	    GameDto dto = input.gameMockDto();
	    dto.setTitle(" ");
	    Exception exception = assertThrows(CustomizedBadRequestException.class, () -> gameService.createGame(dto));
	    
	    String expectedMessage = "Mandatory title.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testUpdateGameNullId() {
	    GameDto dto = input.gameMockDto();
	    dto.setId(null);
	    Exception exception = assertThrows(CustomizedBadRequestException.class, () -> gameService.updateGame(dto));
	    
	    String expectedMessage = "Game ID is required.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testUpdateGameNotFound() {
	    GameDto dto = input.gameMockDto();
	    dto.setId(999L);
	    when(gameRepository.findById(999L)).thenReturn(Optional.empty());

	    Exception exception = assertThrows(GameNotFoundException.class, () -> gameService.updateGame(dto));
	    
	    String expectedMessage = "The requested game was not found in the system.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testDeleteGameNullId() {
	    assertThrows(CustomizedBadRequestException.class, () -> gameService.deleteGame(null));
	}

	@Test
	void testDeleteGameNotFound() {
	    when(gameRepository.findById(123L)).thenReturn(Optional.empty());
	    Exception exception = assertThrows(GameNotFoundException.class, () -> gameService.deleteGame(123L));
	    
	    String expectedMessage = "Game with ID " + 123 + " not found.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}



}
