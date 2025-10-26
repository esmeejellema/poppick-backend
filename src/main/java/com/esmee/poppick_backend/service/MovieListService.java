package com.esmee.poppick_backend.service;

import com.esmee.poppick_backend.dto.CreateMovieListDto;
import com.esmee.poppick_backend.exception.MovieListNotFoundException;
import com.esmee.poppick_backend.exception.MovieNotFoundException;
import com.esmee.poppick_backend.exception.UserNotFoundException;
import com.esmee.poppick_backend.model.Movie;
import com.esmee.poppick_backend.model.MovieList;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.MovieListRepository;
import com.esmee.poppick_backend.repository.MovieRepository;
import com.esmee.poppick_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieListService {

    private final MovieListRepository movieListRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public MovieListService(MovieListRepository movieListRepository,
                            MovieRepository movieRepository,
                            UserRepository userRepository) {
        this.movieListRepository = movieListRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public MovieList createList(CreateMovieListDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        MovieList newList = new MovieList();
        newList.setListName(dto.getListName());
        newList.setUser(user);

        return movieListRepository.save(newList);
    }

    public List<MovieList> getAllListsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return movieListRepository.findByUser_Id(user.getId());
    }

    public MovieList addMoviesToList(long listId, List<Long> movieIds) {
        MovieList list = movieListRepository.findById(listId)
                .orElseThrow(() -> new MovieListNotFoundException("Movie list not found"));

        List<Movie> movies = movieRepository.findAllById(movieIds);
        if (movies.isEmpty()) {
            throw new MovieNotFoundException("No movies found for the given IDs");
        }

        list.getMovies().addAll(movies);
        return movieListRepository.save(list);
    }


    public void deleteList(Long listId) {
        if (!movieListRepository.existsById(listId)) {
            throw new MovieListNotFoundException("Movie list not found");
        }
        movieListRepository.deleteById(listId);
    }
//    public MovieList getMovieList(Long id) {
//        return movieListRepository.findById(id)
//                .orElseThrow(() -> new MovieListNotFoundException("MovieList with id " + id + " not found"));
//    } gebruikt voor testing met Postman. Hoort bij MovieListController get method waarbij je ("{id}") toevoegt aan endpoint.
}
