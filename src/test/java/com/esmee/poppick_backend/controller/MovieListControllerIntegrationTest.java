package com.esmee.poppick_backend.controller;

import com.esmee.poppick_backend.model.MovieList;
import com.esmee.poppick_backend.repository.MovieListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MovieListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieListRepository movieListRepository;

    @Test
    void getAllMovieLists_shouldReturnOk() throws Exception {
        // Arrange: testdata opslaan in H2 (in-memory database)
        MovieList list = new MovieList();
        list.setListName("IntegrationTestList");
        movieListRepository.save(list);

        // Act & Assert: via MockMvc endpoint aanroepen
        mockMvc.perform(get("/movielists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].listName").value("IntegrationTestList"));
    }
}
