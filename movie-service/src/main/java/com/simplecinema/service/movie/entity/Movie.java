package com.simplecinema.service.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
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
    @NotBlank(message = "Imdb ID must not be blank")
    public String imdbId;
    @TextIndexed(weight = 10)
    @NotBlank(message = "Title must not be blank")
    private String title;
    @TextIndexed(weight = 1)
    private String overview;
    private Integer runningTime;
    private List<String> genres;
    private LocalDate releaseDate;
    private List<String> imageUrls;
    @LastModifiedDate
    private Date updatedDate;
}
