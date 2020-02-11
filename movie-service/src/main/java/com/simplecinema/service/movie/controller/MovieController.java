package com.simplecinema.service.movie.controller;

import com.simplecinema.service.movie.domain.Movie;
import com.simplecinema.service.movie.repository.MovieRepository;
import com.simplecinema.service.movie.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Gary Cheng
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private static final String MSG_MOVIE_NOT_FOUND = "Movie not found";
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> byId(@PathVariable("id") String id) {
        log.debug("find movie by id:{}", id);
        return movieRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .defaultIfEmpty(this.responseEntity(HttpStatus.NOT_FOUND, MSG_MOVIE_NOT_FOUND));
    }

    @GetMapping("/imdbId/{imdbId}")
    public Mono<ResponseEntity<?>> byImdbId(@PathVariable("imdbId") String imdbId) {
        log.debug("find movie by imdbId:{}", imdbId);
        return this.movieRepository.findByImdbId(imdbId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .defaultIfEmpty(this.responseEntity(HttpStatus.NOT_FOUND, MSG_MOVIE_NOT_FOUND));
    }

    @GetMapping("/search/{keyword}")
    @ResponseBody
    public Flux<Movie> search(@PathVariable("keyword") String keyword) {
        log.debug("Received search movie request, keyword:{}", keyword);
        return this.movieRepository.searchByKeyword(keyword);
    }

    @PostMapping("/")
    @ResponseBody
    public Mono<Movie> add(@RequestBody Movie movie) {
        log.debug("Received add movie request, movie:{}", movie);
        return movieRepository.save(movie);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> update(@PathVariable("id") String id, @RequestBody Movie movie) {
        log.debug("Update movie by id:{}", id);
        return movieRepository.findById(id)
                .map(m -> {
                    m.setTitle(movie.getTitle());
                    m.setOverview(movie.getOverview());
                    m.setReleaseDate(movie.getReleaseDate());
                    m.setRunningTime(movie.getRunningTime());
                    m.setGenres(movie.getGenres());
                    m.setImageUrls(movie.getImageUrls());
                    return m;
                })
                .flatMap(this.movieRepository::save)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .defaultIfEmpty(this.responseEntity(HttpStatus.NOT_FOUND, MSG_MOVIE_NOT_FOUND));
    }

    private ResponseEntity<?> responseEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ApiResponse(httpStatus.value(), message), httpStatus);
    }
}
