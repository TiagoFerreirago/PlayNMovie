package com.th.playnmovie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.th.playnmovie.dto.FavoriteDto;
import com.th.playnmovie.mapper.FavoriteMapper;
import com.th.playnmovie.model.Favorite;
import com.th.playnmovie.repository.FavoriteRepository;

@Service
public class FavoriteService {
	
	private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

	@Autowired
	private FavoriteRepository favoriteRepository;
	
	public FavoriteDto favorite(FavoriteDto favoriteDto) throws Exception {
		 logger.info("Attempting to add favorite: userId={}, itemId={}, type={}",
	                favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		 Favorite favorite;

		    boolean favoritaded = favoriteRepository.existsByUserAndItemIdAndType(
		        favoriteDto.getUser(),
		        favoriteDto.getItemId(),
		        favoriteDto.getType()
		    );

		    if (favoritaded) {
		    	 logger.warn("Favorite already exists for userId={}, itemId={}, type={}",
		                    favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		        throw new Exception();
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
	
	public void unFavorite(FavoriteDto favoriteDto) throws NotFoundException {
	    logger.info("Attempting to remove favorite: userId={}, itemId={}, type={}",
                favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
		
		boolean exists = favoriteRepository.existsByUserAndItemIdAndType(favoriteDto.getUser(), favoriteDto.getItemId(), favoriteDto.getType());
		if(!exists) {
			logger.warn("Favorite not found for removal: userId={}, itemId={}, type={}",
	                    favoriteDto.getUser().getId(), favoriteDto.getItemId(), favoriteDto.getType());
			throw new NotFoundException();
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
