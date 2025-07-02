	package com.th.playnmovie.service;
	
	import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.core.ParameterizedTypeReference;
	import org.springframework.http.HttpHeaders;
	import org.springframework.stereotype.Service;
	import org.springframework.web.reactive.function.client.WebClient;
	
	import com.th.playnmovie.dto.GameDto;
import com.th.playnmovie.exception.CustomizedBadRequestException;
import com.th.playnmovie.mapper.GameMapper;
	import com.th.playnmovie.model.Game;
import com.th.playnmovie.model.GenreEnum;
import com.th.playnmovie.repository.GameRepository;
	
	import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
	
@Service
public class IgdbService {

    private static final Logger logger = LoggerFactory.getLogger(IgdbService.class);

	
	private WebClient webClient;
	@Autowired
	private GameRepository gameRepository;
	@Value("${token.key.clientid}")
	private String clientId;
	@Value("${token.key.authorization}")
	private String authorization;

	public IgdbService(WebClient.Builder webClientBuilder ) {
		this.webClient = webClientBuilder.baseUrl("https://api.igdb.com/v4/").build();
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Client-ID", clientId);
		headers.set("Authorization", authorization);
		return headers;
	}
	
	public Flux<GameDto> getAllGames(int page){
		logger.info("Requisição para buscar jogos da API IGDB na página {}", page);

		if (page < 0) {
	        throw new CustomizedBadRequestException("Número da página não pode ser negativo.");
	    }
		
		int offset = page * 5;
		Flux<GameDto> web = webClient.post()
			    .uri("/games")
			    .headers(headers -> headers.addAll(getHeaders()))
			    .bodyValue("fields id, name, genres, release_dates.date, release_dates.platform, release_dates.region, release_dates.id, summary, cover.url; limit 5; offset "+offset+";")
			    .retrieve()
			    .onStatus(status -> status.isError(), clientResponse -> {
			    	 logger.error("Erro ao consultar API externa IGDB. Status: {}", clientResponse.statusCode());
			    	return Mono.error(new CustomizedBadRequestException("Erro ao consultar API externa"));})
			    .bodyToMono(new ParameterizedTypeReference<List<GameDto>>() {})
			    .flatMapMany(Flux::fromIterable);
		return web;
			
	}
	
	public Flux<GameDto> findByNameOrGenres(String name, List<String> genres) {
		logger.info("Iniciando busca por nome '{}' ou gêneros: {}", name, genres);
		
		if ((name == null || name.isBlank()) && (genres == null || genres.isEmpty())) {
		    logger.warn("Parâmetros de busca inválidos: nome e gêneros ausentes.");
	        throw new CustomizedBadRequestException("Informe pelo menos um nome ou gêneros para busca.");
	    }
		
		if (genres != null) {
			 logger.debug("Validando gêneros fornecidos: {}", genres);
	        for (String genre : genres) {
	        	if (genre == null || genre.isBlank()) {
	        		logger.warn("Gênero inválido encontrado (nulo ou vazio).");
	                throw new CustomizedBadRequestException("Gênero inválido: valor vazio ou nulo");
	            }
	            boolean valid = Arrays.stream(GenreEnum.values())
	                .anyMatch(g -> g.getName().equalsIgnoreCase(genre));
	            if (!valid) {
	            	logger.warn("Gênero inválido: {}", genre);
	                throw new CustomizedBadRequestException("Gênero inválido: " + genre);
	            }
	        }
	    }
	    logger.debug("Consultando jogos no banco de dados local...");
	    List<Game> localGames = findByNameOrGenresInLocalDb(name, genres);
	    if (!localGames.isEmpty()) {
	        logger.info("Jogos encontrados localmente: {}", localGames.size());
	        return Flux.fromIterable(localGames.stream().map(GameMapper::toDto).toList());
	    }

	    int MAX_PAGES = 10;

	    return Flux.range(0, MAX_PAGES)
	            .delayElements(Duration.ofMillis(500))
	            .flatMap(this::getAllGames)
	            .filter(game -> {
	                boolean nameMatches = name != null && game.getTitle() != null && game.getTitle().equalsIgnoreCase(name);
	                boolean genreMatches = genres != null && !genres.isEmpty()
	                        && game.getGenres() != null
	                        && game.getGenres().stream().anyMatch(g -> genres.stream()
	                                .anyMatch(inputGenre -> inputGenre.equalsIgnoreCase(g)));
	                return nameMatches || genreMatches;
	            })
	            .distinct(GameDto::getId)
	            .take(20)
	            .doOnComplete(() -> logger.info("Busca na API IGDB concluída"))
	            .switchIfEmpty(Flux.fromIterable(localGames.stream().map(GameMapper::toDto).toList()));
	}

	
	public List<Game> findByNameOrGenresInLocalDb(String name, List<String> genres){
		logger.debug("Consultando banco de dados local com nome='{}' e gêneros={}", name, genres);
		return gameRepository.findGames(name, genres);
	}
}
