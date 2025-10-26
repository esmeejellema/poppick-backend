package com.esmee.poppick_backend.dto;

public class RecommendationDto {
    private Long id;
    private Long userId;
    private Long movieId;
    private String movieTitle;

    public RecommendationDto(Long id, Long userId, Long movieId, String movieTitle) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
}


