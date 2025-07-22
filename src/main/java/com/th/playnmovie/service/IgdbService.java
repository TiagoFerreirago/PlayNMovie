package com.th.playnmovie.service;
	
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.exception.response.ErrorMessages;
import com.th.playnmovie.mapper.GameMapper;
import com.th.playnmovie.model.Game;
import com.th.playnmovie.model.GenreEnum;
import com.th.playnmovie.repository.GameRepository;
	
import reactor.core.publisher.Flux;
	
@Service
public class IgdbService {

    private static final Logger logger = LoggerFactory.getLogger(IgdbService.class);
    private static final int MAX_API_PAGES = 10;

    @Autowired private GameRepository gameRepository;
    @Autowired private IgdbClient igdbClient;

    public Flux<GameDto> findByNameOrGenres(String name, List<String> genres) {
        logger.info("Starting search by name '{}' or genres: {}", name, genres);

        if ((name == null || name.isBlank()) && (genres == null || genres.isEmpty())) {
            logger.warn("Invalid search parameters.");
            throw new CustomizedBadRequestException(ErrorMessages.SEARCH_CRITERIA_REQUIRED);
        }

        validateGenres(genres);

        List<Game> localGames = searchInLocalDb(name, genres);
        if (!localGames.isEmpty()) {
            logger.info("Found {} games in local DB", localGames.size());
            return Flux.fromIterable(localGames).map(GameMapper::toDto);
        }

        return Flux.range(0, MAX_API_PAGES)
                .delayElements(Duration.ofMillis(500))
                .flatMap(igdbClient::getAllGames)
                .filter(game -> matchesNameOrGenre(game, name, genres))
                .distinct(GameDto::getId)
                .take(20)
                .doOnComplete(() -> logger.info("IGDB API search complete"))
                .switchIfEmpty(Flux.fromIterable(localGames).map(GameMapper::toDto));
    }

    public List<Game> searchInLocalDb(String name, List<String> genres) {
        logger.debug("Searching local DB with name='{}' and genres={}", name, genres);
        return gameRepository.findGames(name, genres);
    }

    private void validateGenres(List<String> genres) {
        if (genres == null) return;

        for (String genre : genres) {
            if (genre == null || genre.isBlank()) {
                throw new CustomizedBadRequestException(ErrorMessages.EMPTY_GENRE);
            }
            boolean valid = Arrays.stream(GenreEnum.values())
                    .anyMatch(g -> g.getName().equalsIgnoreCase(genre));
            if (!valid) {
                throw new CustomizedBadRequestException(ErrorMessages.INVALID_GENRE + genre);
            }
        }
    }

    private boolean matchesNameOrGenre(GameDto game, String name, List<String> genres) {
        boolean nameMatches = name != null && game.getTitle() != null &&
                              game.getTitle().equalsIgnoreCase(name);

        boolean genreMatches = genres != null && !genres.isEmpty()
                && game.getGenres() != null
                && game.getGenres().stream()
                        .anyMatch(g -> genres.stream().anyMatch(input -> input.equalsIgnoreCase(g)));

        return nameMatches || genreMatches;
    }
}

