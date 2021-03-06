package com.simplecinema.service.movie.repository;

import com.simplecinema.service.movie.entity.Movie;
import reactor.core.publisher.Flux;

/**
 * @author Gary Cheng
 */
public interface SearchOperations {
    Flux<Movie> searchByKeyword(String keyword);
}
