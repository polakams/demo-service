package com.polakams.demoservice.book;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createGetDelete_roundTrip() throws Exception {
        String body = """
                {
                  "title": "Working Effectively with Legacy Code",
                  "author": "Michael Feathers",
                  "isbn": "9780131177055",
                  "publishedYear": 2004
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Working Effectively with Legacy Code"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        Number id = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780131177055"));

        mockMvc.perform(delete("/api/v1/books/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/books/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void seededBooks_areAvailable() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }
}
