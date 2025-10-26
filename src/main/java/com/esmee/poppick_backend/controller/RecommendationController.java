package com.esmee.poppick_backend.controller;

import com.esmee.poppick_backend.dto.RecommendationDto;
import com.esmee.poppick_backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    //save recommendation
    @PostMapping
    public ResponseEntity<RecommendationDto> createRecommendation(
            @RequestParam Long userId,
            @RequestParam Long movieId) {

        RecommendationDto recommendation =
                recommendationService.createRecommendation(userId, movieId);
        System.out.println("Received userId: " + userId + ", movieId: " + movieId);

        return ResponseEntity.ok(recommendation);

    }

    //get list of saved recommendations
    @GetMapping("/{userId}")
    public ResponseEntity<List<RecommendationDto>> getRecommendations(@PathVariable Long userId) {
        List<RecommendationDto> recommendations =
                recommendationService.getRecommendationsByUser(userId);

        return ResponseEntity.ok(recommendations);
    }
}
