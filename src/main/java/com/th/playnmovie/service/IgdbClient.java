package com.th.playnmovie.service;

import com.th.playnmovie.dto.GameDto;

import reactor.core.publisher.Flux;

public interface IgdbClient {
	
    Flux<GameDto> getAllGames(int page);
}
