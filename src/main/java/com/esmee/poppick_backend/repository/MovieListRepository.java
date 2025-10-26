package com.esmee.poppick_backend.repository;

import com.esmee.poppick_backend.model.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {
    List<MovieList> findByUser_Id(Long userId);
}
