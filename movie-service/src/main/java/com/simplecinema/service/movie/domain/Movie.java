package com.simplecinema.service.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
    @Id
    public String id;
    @TextIndexed
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
