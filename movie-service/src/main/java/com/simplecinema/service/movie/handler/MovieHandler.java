package com.simplecinema.service.movie.handler;

import com.simplecinema.service.movie.domain.Movie;
import com.simplecinema.service.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
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

    private Mono<ServerResponse> create(ServerRequest request) {
        log.debug("Received create Movie request");
        return request.bodyToMono(Movie.class)
                .flatMap(this.movieRepository::save)
                .flatMap(this::buildResponse);
    }

    private Mono<ServerResponse> update(ServerRequest request) {
        log.debug("Received update blog request");
        String id = request.pathVariable("id");
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
