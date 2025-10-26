package com.esmee.poppick_backend.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_lists")
public class MovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //
    private Long id;

    @Column (name = "list_name", nullable = false)
    private String listName;

    @ManyToMany
    @JoinTable(
            name = "movie_list_movies",
            joinColumns = @JoinColumn(name = "movie_list_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<Movie> movies = new ArrayList<>();

    public MovieList(Long id, String listName) {
        this.id = id;
        this.listName = listName;
    }

    public MovieList() {

    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getListName() {
        return listName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public List<Movie> getMovies() {
        return movies;
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}


