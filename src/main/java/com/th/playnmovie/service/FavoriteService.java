package com.th.playnmovie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.exception.FavoriteAlreadyExistsException;
import com.th.playnmovie.exception.FavoriteNotFoundException;
import com.th.playnmovie.mapper.FavoriteMapper;
import com.th.playnmovie.mapper.UserMapper;
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.repository.FavoriteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.th.playnmovie.security.model.User;

@Service
public class FavoriteService {
	
	private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

	@Autowired
	private FavoriteRepository favoriteRepository;
	
	public FavoriteDto addToFavorites(FavoriteDto favoriteDto) {
		
		if (favoriteDto.getUser() == null) {
		        throw new IllegalArgumentException("User must not be null.");
		 }
		
		Object main = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!(main instanceof User)) {
		        throw new IllegalArgumentException("Invalid authenticated user.");
		 }
		 User authenticatedUser = (User) main;
		 
		 favoriteDto.setUser(UserMapper.tokenResponseVo(authenticatedUser));

		 logger.info("Attempting to add favorite: userId={}, itemId={}, type={}",
	                authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		 Favorite favorite;
		 boolean alreadyFavorited = favoriteRepository.existsByUserAndItemIdAndType(authenticatedUser, favoriteDto.getItemId(), favoriteDto.getType());

		if (alreadyFavorited) {
		    logger.warn("Favorite already exists for userId={}, itemId={}, type={}",
                authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
        	throw new FavoriteAlreadyExistsException();
		}
		
        favorite = new Favorite();
        favorite.setItemId(favoriteDto.getItemId());
        favorite.setType(favoriteDto.getType());
        favorite.setUser(authenticatedUser);
        favorite = favoriteRepository.save(favorite);
        logger.info("Favorite successfully created: id={}, userId={}, itemId={}, type={}",
                favorite.getId(), authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
	

		return FavoriteMapper.toDto(favorite);
	}
	
	public void removeFromFavorites(FavoriteDto favoriteDto) {
	    Object main = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(!(main instanceof User)) {
		        throw new IllegalArgumentException("Invalid authenticated user.");
		 }
		User authenticatedUser = (User) main;
		Favorite favorite = FavoriteMapper.fromDto(favoriteDto);
		 
		logger.info("Attempting to remove favorite: userId={}, itemId={}, type={}",
                authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		boolean exists = favoriteRepository.existsByUserAndItemIdAndType(favorite.getUser(), favorite.getItemId(), favorite.getType());
		if(!exists) {
			logger.warn("Favorite not found for removal: userId={}, itemId={}, type={}",
	                    authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
			throw new FavoriteNotFoundException();
		}
		
		favoriteRepository.unfavorite(authenticatedUser, favoriteDto.getItemId(), favoriteDto.getType());
		logger.info("Favorite successfully removed: userId={}, itemId={}, type={}",
	                authenticatedUser.getId(), favoriteDto.getItemId(), favoriteDto.getType());
	}
	
	public List<FavoriteDto> findAll(){
        logger.info("Retrieving all favorites");

		Object main = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User authenticatedUser = (User) main;
		
		List<Favorite> favorites = favoriteRepository.findByUser(authenticatedUser);
		
    	logger.info("Found {} favorites for userId={}", favorites.size(), authenticatedUser.getId());
		return favorites.stream().map(p -> FavoriteMapper.toDto(p)).collect(Collectors.toList());
	}
	
}
