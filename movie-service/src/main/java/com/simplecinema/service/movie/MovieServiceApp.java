package com.simplecinema.service.movie;

import com.simplecinema.service.movie.handler.MovieHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@SpringBootApplication
@EnableDiscoveryClient
public class MovieServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApp.class, args);
    }

    //@Bean
    public RouterFunction<ServerResponse> routes(
            MovieHandler movieHandler) {
        return nest(path("/api/v1/movies"), movieHandler.getRouterFunction());
    }
}
