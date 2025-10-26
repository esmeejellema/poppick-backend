package com.esmee.poppick_backend.controller;
import com.esmee.poppick_backend.dto.CreateMovieListDto;
import com.esmee.poppick_backend.dto.AddMoviesToListDto;
import com.esmee.poppick_backend.model.MovieList;
import com.esmee.poppick_backend.service.MovieListService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Transactional
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/movielists")  // connects to port and base URL path for all methods
public class MovieListController {

    private final MovieListService movieListService;

    public MovieListController(MovieListService movieListService) {
        this.movieListService = movieListService;
    }

    // POST /movielists - create a new user list
    @PostMapping
    public ResponseEntity<MovieList> createList(@RequestBody CreateMovieListDto dto) {//movieList is an object that SB creates here and attaches the name from the function Payload on FE to it. MovieList is entity movieList is object.
        System.out.println(">>> Received movieList:");
        System.out.println("Name: " + dto.getListName());//so this would be the name given to a new list and attached to a newly created object

        if (dto.getListName() == null || dto.getListName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        MovieList savedList = movieListService.createList(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);

    }
    // GET /movielists
    @GetMapping //dit endpoint is nieuw.
    public List<MovieList> getLists() {
        return movieListService.getAllListsForCurrentUser();
    }
    //alleen gebruiker ziet eigen lijsten

    @PutMapping("/{selectedListId}/movies")//same endpoint as fetch function FE
    public ResponseEntity<MovieList> addMoviesToList(@PathVariable Long selectedListId, @RequestBody AddMoviesToListDto dto) {

        System.out.println("Received movie IDs: " + dto.getMovieIds());
        System.out.println("Selected List ID: " + selectedListId);

        MovieList updatedList = movieListService.addMoviesToList(selectedListId, dto.getMovieIds());
        return ResponseEntity.ok(updatedList);
    }
    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        movieListService.deleteList(listId);
        return ResponseEntity.noContent().build(); // 204
    }


}
