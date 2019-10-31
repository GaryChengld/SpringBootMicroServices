package com.simplecinema.service.movie.repository;

import com.simplecinema.service.movie.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author Gary Cheng
 */
public interface MovieRepository extends ReactiveMongoRepository<Movie, String>, SearchOperations {

    Mono<Movie> findByImdbId(String imdbId);
}
