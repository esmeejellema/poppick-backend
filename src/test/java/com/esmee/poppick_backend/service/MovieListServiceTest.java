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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieListServiceTest {

    @Mock
    private MovieListRepository movieListRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private MovieListService movieListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    // ---------- createList() ----------
    @Test
    void createList_shouldSaveMovieList_whenUserExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        CreateMovieListDto dto = new CreateMovieListDto();
        dto.setListName("My List");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(movieListRepository.save(any(MovieList.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        MovieList result = movieListService.createList(dto);

        // Assert
        assertEquals("My List", result.getListName());
        assertEquals(user, result.getUser());
        verify(movieListRepository).save(any(MovieList.class));
    }

    @Test
    void createList_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        CreateMovieListDto dto = new CreateMovieListDto();

        // Act + Assert
        assertThrows(UserNotFoundException.class, () -> movieListService.createList(dto));
    }

    // ---------- getAllListsForCurrentUser() ----------
    @Test
    void getAllListsForCurrentUser_shouldReturnLists_whenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(movieListRepository.findByUser_Id(1L)).thenReturn(List.of(new MovieList()));

        // Act
        List<MovieList> result = movieListService.getAllListsForCurrentUser();

        // Assert
        assertEquals(1, result.size());
        verify(movieListRepository).findByUser_Id(1L);
    }

    @Test
    void getAllListsForCurrentUser_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFoundException.class, () -> movieListService.getAllListsForCurrentUser());
    }

    // ---------- addMoviesToList() ----------
    @Test
    void addMoviesToList_shouldAddMovies_whenListAndMoviesExist() {
        // Arrange
        MovieList list = new MovieList();
        list.setMovies(new java.util.ArrayList<>());

        Movie movie = new Movie();
        movie.setId(10L);

        when(movieListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(movieRepository.findAllById(List.of(10L))).thenReturn(List.of(movie));
        when(movieListRepository.save(any(MovieList.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        MovieList result = movieListService.addMoviesToList(1L, List.of(10L));

        // Assert
        assertEquals(1, result.getMovies().size());
        verify(movieListRepository).save(list);
    }

    @Test
    void addMoviesToList_shouldThrowException_whenListNotFound() {
        // Arrange
        when(movieListRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(MovieListNotFoundException.class, () -> movieListService.addMoviesToList(1L, List.of(1L)));
    }

    @Test
    void addMoviesToList_shouldThrowException_whenNoMoviesFound() {
        // Arrange
        MovieList list = new MovieList();
        list.setMovies(new java.util.ArrayList<>());

        when(movieListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(movieRepository.findAllById(List.of(1L))).thenReturn(List.of());

        // Act + Assert
        assertThrows(MovieNotFoundException.class, () -> movieListService.addMoviesToList(1L, List.of(1L)));
    }

    // ---------- deleteList() ----------
    @Test
    void deleteList_shouldDelete_whenListExists() {
        // Arrange
        when(movieListRepository.existsById(1L)).thenReturn(true);

        // Act
        movieListService.deleteList(1L);

        // Assert
        verify(movieListRepository).deleteById(1L);
    }

    @Test
    void deleteList_shouldThrowException_whenListNotFound() {
        // Arrange
        when(movieListRepository.existsById(1L)).thenReturn(false);

        // Act + Assert
        assertThrows(MovieListNotFoundException.class, () -> movieListService.deleteList(1L));
    }
}
