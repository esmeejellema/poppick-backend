package com.esmee.poppick_backend.controller;

import com.esmee.poppick.model.Movie;
import com.esmee.poppick.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@Transactional
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

//    @PostMapping
//    public Movie createList(@RequestBody Movie list) {
//        return movieRepository.save(list);
//    }
}
