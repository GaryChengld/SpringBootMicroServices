package com.simplecinema.service.movie.handler;

import com.simplecinema.service.movie.domain.Movie;
import com.simplecinema.service.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Gary Cheng
 */
@Component
@Slf4j
public class MovieHandler {
    private final MovieRepository movieRepository;

    public MovieHandler(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public RouterFunction<ServerResponse> getRouterFunction() {
        return nest(accept(APPLICATION_JSON),
                route(GET("/{id}"), this::byId)
                        .andRoute(GET("/imdbId/{imdbId}"), this::byImdbId)
                        .andRoute(GET("/search/{keyword}"), this::search)
                        .andRoute(POST("/"), this::create)
                        .andRoute(PUT("/{id}"), this::update)
        );
    }

    private Mono<ServerResponse> byId(ServerRequest request) {
        String id = request.pathVariable("id");
        log.debug("find movie by id:{}", id);
        return this.movieRepository.findById(id)
                .flatMap(this::buildResponse)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> byImdbId(ServerRequest request) {
        String imdbId = request.pathVariable("imdbId");
        log.debug("find movie by imdbId:{}", imdbId);
        return this.movieRepository.findByImdbId(imdbId)
                .flatMap(this::buildResponse)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> search(ServerRequest request) {
        String keyword = request.pathVariable("keyword");
        log.debug("Received search movie request, keyword:{}", keyword);
        Flux<Movie> movies = this.movieRepository.searchByKeyword(keyword);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(movies, Movie.class);
    }

    private Mono<ServerResponse> create(ServerRequest request) {
        log.debug("Received create movie request");
        return request.bodyToMono(Movie.class)
                .flatMap(this.movieRepository::save)
                .flatMap(this::buildResponse);
    }

    private Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        log.debug("Received update movie request, id={}", id);
        AtomicReference<Movie> movieRef = new AtomicReference<>();
        return request.bodyToMono(Movie.class)
                .doOnNext(movieRef::set)
                .flatMap(m -> this.movieRepository.findById(id))
                .map(m -> {
                    Movie movie = movieRef.get();
                    m.setTitle(movie.getTitle());
                    m.setOverview(movie.getOverview());
                    m.setReleaseDate(movie.getReleaseDate());
                    m.setRunningTime(movie.getRunningTime());
                    m.setGenres(movie.getGenres());
                    m.setImageUrls(movie.getImageUrls());
                    return m;
                })
                .flatMap(this.movieRepository::save)
                .flatMap(this::buildResponse)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private <T> Mono<ServerResponse> buildResponse(T body) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromValue(body));
    }
}
