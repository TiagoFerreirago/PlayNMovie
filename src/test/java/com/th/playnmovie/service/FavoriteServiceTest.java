package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.exception.FavoriteAlreadyExistsException;
import com.th.playnmovie.exception.FavoriteNotFoundException;
import com.th.playnmovie.mock.FavoriteMock;
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.repository.FavoriteRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	private FavoriteMock input;
	@Mock
	private FavoriteRepository favoriteRepository;
	@InjectMocks
	private FavoriteService favoriteService;
	
	@BeforeEach
	void setUp() throws Exception {
		input = new FavoriteMock();
	}

	@Test
	void  testAddToFavorites() {

		Favorite favorite = input.favoriteMock(1L);
		favorite.setId(1L);
		
		FavoriteDto dto = input.favoriteMockDto(1L);
		favorite.setId(1L);
		
		when(favoriteRepository.existsByUserAndItemIdAndType(dto.getUser(), dto.getItemId(), dto.getType())).thenReturn(false);
		when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
		
		var result = favoriteService.addToFavorites(dto);
		
		assertNotNull(result);
		assertNotNull(result.getUser());
	    assertEquals(3L, result.getItemId());
	    assertEquals("Game", result.getType().getName());
	    verify(favoriteRepository).save(any(Favorite.class));
	   
	}

	@Test
	void testRemoveFromFavorites() {

		FavoriteDto dto = input.favoriteMockDto(1L);
		dto.setId(1L);
		
		when(favoriteRepository.existsByUserAndItemIdAndType(dto.getUser(), dto.getItemId(), dto.getType()))
        .thenReturn(true);

		favoriteService.removeFromFavorites(dto);
	}

	@Test
	void testFindAll() {
	    List<Favorite> favorites = input.favoriteMockList();

	    when(favoriteRepository.findAll()).thenReturn(favorites);

	    var result = favoriteService.findAll();

	    assertNotNull(result);
	    assertEquals(10, result.size());

	    FavoriteDto favoriteOne = result.get(1);
	    assertEquals(1L, favoriteOne.getId());
	    assertEquals(3L, favoriteOne.getItemId());
	    assertEquals("Game", favoriteOne.getType().getName());
	    assertNotNull(favoriteOne.getUser());

	    FavoriteDto favoriteFour = result.get(3);
	    
	    assertEquals(3L, favoriteFour.getId());
	    assertEquals(5L, favoriteFour.getItemId());
	    assertEquals("Game", favoriteFour.getType().getName());
	    assertNotNull(favoriteFour.getUser());

	    FavoriteDto favoriteSix = result.get(5);
	    assertEquals(5L, favoriteSix.getId());
	    assertEquals(7L, favoriteSix.getItemId());
	    assertEquals("Game", favoriteSix.getType().getName());
	    assertNotNull(favoriteSix.getUser());
	}
	
	@Test
	void testUnFavoriteVerifyInteraction() {
	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(dto.getUser(), dto.getItemId(), dto.getType()))
	        .thenReturn(true);

	    favoriteService.removeFromFavorites(dto);

	    verify(favoriteRepository).unfavorite(dto.getUser(), dto.getItemId(), dto.getType());
	}
	
	@Test
	void testAddToFavoritesThrowsWhenAlreadyFavorited() {
	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(dto.getUser(), dto.getItemId(), dto.getType()))
	        .thenReturn(true);

	    Exception exception = assertThrows(FavoriteAlreadyExistsException.class, () -> {
	        favoriteService.addToFavorites(dto);
	    });
	    
	    String expectedMessage = "Item is already marked as favorite.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}

	@Test
	void testUnFavoriteNotFound() {
	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(dto.getUser(), dto.getItemId(), dto.getType()))
	        .thenReturn(false);

	    Exception exception = assertThrows(FavoriteNotFoundException.class, () -> {
	        favoriteService.removeFromFavorites(dto);
	    });
	    
	    String expectedMessage = "The requested favorite was not found in the system.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}
	
	@Test
	void testFavoriteNullUser() {
	    FavoriteDto dto = input.favoriteMockDto(1L);
	    dto.setUser(null);

	    Exception exception = assertThrows(IllegalArgumentException.class, () -> favoriteService.addToFavorites(dto));
	    
	    String expectedMessage = "User must not be null.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}

}
