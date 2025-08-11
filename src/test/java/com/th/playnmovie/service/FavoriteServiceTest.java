package com.th.playnmovie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.exception.FavoriteAlreadyExistsException;
import com.th.playnmovie.exception.FavoriteNotFoundException;
import com.th.playnmovie.mapper.FavoriteMapper;
import com.th.playnmovie.mapper.UserMapper;
import com.th.playnmovie.mock.FavoriteMock;
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.repository.FavoriteRepository;
import com.th.playnmovie.security.model.User;

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
		
		mockAuthenticatedUser(1L, "test");

		Favorite favorite = input.favoriteMock(1L);
		favorite.setId(1L);
		FavoriteDto dto = FavoriteMapper.toDto(favorite);

		
		when(favoriteRepository.existsByUserAndItemIdAndType(any(User.class), eq(dto.getItemId()), eq(dto.getType())))
	    .thenReturn(false);
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
		
		mockAuthenticatedUser(1L, "test");

		FavoriteDto dto = input.favoriteMockDto(1L);
		dto.setId(1L);
		
		when(favoriteRepository.existsByUserAndItemIdAndType(UserMapper.tokenResponse(dto.getUser()), dto.getItemId(), dto.getType()))
        .thenReturn(true);

		favoriteService.removeFromFavorites(dto);
	}

	@Test
	void testFindAll() {
		
		mockAuthenticatedUser(1L, "test");
		
	    List<Favorite> favorites = input.favoriteMockList();

	    when(favoriteRepository.findByUser(any(User.class))).thenReturn(favorites);

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
		
		mockAuthenticatedUser(1L, "test");
		
	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(any(User.class), eq(dto.getItemId()), eq(dto.getType())))
	        .thenReturn(true);

	    favoriteService.removeFromFavorites(dto);

	    verify(favoriteRepository).unfavorite(any(User.class), eq(dto.getItemId()), eq(dto.getType()));
	}
	
	@Test
	void testAddToFavoritesThrowsWhenAlreadyFavorited() {
		
		mockAuthenticatedUser(1L, "test");

	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(
	            any(User.class), eq(dto.getItemId()), eq(dto.getType())))
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
		
		mockAuthenticatedUser(1L, "test");
	    FavoriteDto dto = input.favoriteMockDto(1L);

	    when(favoriteRepository.existsByUserAndItemIdAndType(UserMapper.tokenResponse(dto.getUser()), dto.getItemId(), dto.getType()))
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

		mockAuthenticatedUser(1L, "test");

	    FavoriteDto dto = input.favoriteMockDto(1L);
	    dto.setUser(null);

	    Exception exception = assertThrows(IllegalArgumentException.class, () -> favoriteService.addToFavorites(dto));
	    
	    String expectedMessage = "User must not be null.";
	    String messageReleased = exception.getMessage();
	    
	    assertEquals(messageReleased, expectedMessage);
	}
	
	private void mockAuthenticatedUser(Long id, String username) {
	    User userMock = new User();
	    userMock.setId(id);
	    userMock.setUsername(username);

	    Authentication authentication = mock(Authentication.class);
	    lenient().when(authentication.getPrincipal()).thenReturn(userMock);

	    SecurityContext securityContext = mock(SecurityContext.class);
	    lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

	    SecurityContextHolder.setContext(securityContext);
	}

}
