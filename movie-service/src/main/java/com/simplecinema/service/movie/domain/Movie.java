package com.simplecinema.service.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Gary Cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movie")
public class Movie {
    @Id
    public String id;
    @Indexed(unique = true)
    @NotNull(message = "Imdb ID must not be null")
    public String imdbId;
    @TextIndexed
    @NotNull(message = "Title must not be null")
    private String title;
    @TextIndexed
    private String overview;
    private Integer runningTime;
    private List<String> genres;
    private Date releaseDate;
    private List<String> imageUrls;
    @LastModifiedDate
    private Date updatedDate;
}
