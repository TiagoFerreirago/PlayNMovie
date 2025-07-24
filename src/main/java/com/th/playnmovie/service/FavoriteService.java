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
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
	private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

	@Autowired
	private FavoriteRepository favoriteRepository;
	
	public FavoriteDto addToFavorites(FavoriteDto favoriteDto) {
		
		 if (favoriteDto.getUser() == null) {
		        throw new IllegalArgumentException("User must not be null.");
		 }
		 
		 logger.info("Attempting to add favorite: userId={}, itemId={}, type={}",
	                favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		 Favorite favorite;

		    boolean alreadyFavorited = favoriteRepository.existsByUserAndItemIdAndType(
		        favoriteDto.getUser(),
		        favoriteDto.getItemId(),
		        favoriteDto.getType()
		    );

		    if (alreadyFavorited) {
		    	 logger.warn("Favorite already exists for userId={}, itemId={}, type={}",
		                    favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		        throw new FavoriteAlreadyExistsException();
		    } else {
		    	favorite = new Favorite();
		        favorite.setItemId(favoriteDto.getItemId());
		        favorite.setType(favoriteDto.getType());
		        favorite.setUser(favoriteDto.getUser());
		        favorite = favoriteRepository.save(favorite);
		        logger.info("Favorite successfully created: id={}, userId={}, itemId={}, type={}",
	                    favorite.getId(), favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		    }

		    return FavoriteMapper.toDto(favorite);
	}
	
	public void removeFromFavorites(FavoriteDto favoriteDto) {
	    logger.info("Attempting to remove favorite: userId={}, itemId={}, type={}",
                favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		boolean exists = favoriteRepository.existsByUserAndItemIdAndType(favoriteDto.getUser(), favoriteDto.getItemId(), favoriteDto.getType());
		if(!exists) {
			logger.warn("Favorite not found for removal: userId={}, itemId={}, type={}",
	                    favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
			throw new FavoriteNotFoundException();
		}
		
		favoriteRepository.unfavorite(favoriteDto.getUser(), favoriteDto.getItemId(), favoriteDto.getType());
		logger.info("Favorite successfully removed: userId={}, itemId={}, type={}",
	                favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
	}
	
	public List<FavoriteDto> findAll(){
        logger.info("Retrieving all favorites");

		List<Favorite> favorites = favoriteRepository.findAll();
		
		logger.info("Found {} favorites", favorites.size());
		return favorites.stream().map(p -> FavoriteMapper.toDto(p)).collect(Collectors.toList());
	}
	
}
