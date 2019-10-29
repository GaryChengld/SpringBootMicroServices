package com.simplecinema.service.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author Gary Cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Movie")
public class Movie {
    private String id;
    @Indexed(unique = true)
    public String imdbId;
    @TextIndexed
    private String title;
    @TextIndexed
    private String overview;
    private Integer runningTime;
    private List<String> genres;
    private Date releaseDate;
    private List<String> imageUrls;
}
