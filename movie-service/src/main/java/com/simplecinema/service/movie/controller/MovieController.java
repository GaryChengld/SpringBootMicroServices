package com.simplecinema.service.movie.controller;

import com.simplecinema.service.movie.vo.ApiResponse;
import com.simplecinema.service.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<ResponseEntity<ApiResponse>> byId(@PathVariable("id") String id) {
        return movieRepository.findById(id)
                .map(movie -> this.responseEntityByData(movie))
                .defaultIfEmpty(this.responseEntity(HttpStatus.NOT_FOUND, MSG_MOVIE_NOT_FOUND));
    }

    private ResponseEntity<ApiResponse> responseEntityByData(Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(data);
        return ResponseEntity.ok(apiResponse);
    }

    private ResponseEntity<ApiResponse> responseEntity(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ApiResponse(httpStatus.value(), message), httpStatus);
    }
}
