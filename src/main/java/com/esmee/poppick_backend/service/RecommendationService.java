package com.esmee.poppick_backend.service;

import com.esmee.poppick_backend.dto.RecommendationDto;
import com.esmee.poppick_backend.exception.MovieNotFoundException;
import com.esmee.poppick_backend.exception.UserNotFoundException;
import com.esmee.poppick_backend.exception.RecommendationNotFoundException;
import com.esmee.poppick_backend.model.Movie;
import com.esmee.poppick_backend.model.Recommendation;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.MovieRepository;
import com.esmee.poppick_backend.repository.RecommendationRepository;
import com.esmee.poppick_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public RecommendationService(RecommendationRepository recommendationRepository,
                                 MovieRepository movieRepository,
                                 UserRepository userRepository) {
        this.recommendationRepository = recommendationRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RecommendationDto createRecommendation(Long userId, Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID: " + movieId));

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setMovie(movie);

        Recommendation saved = recommendationRepository.save(recommendation);

        return new RecommendationDto(
                saved.getId(),
                user.getId(),
                movie.getId(),
                movie.getTitle()
        );
    }

    public List<RecommendationDto> getRecommendationsByUser(Long userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);

        if (recommendations.isEmpty()) {
            throw new RecommendationNotFoundException(
                    "No recommendations found for user with ID: " + userId
            );
        }

        return recommendations.stream()
                .map(r -> new RecommendationDto(
                        r.getId(),
                        r.getUser().getId(),
                        r.getMovie().getId(),
                        r.getMovie().getTitle()
                ))
                .collect(Collectors.toList());
    }
}
