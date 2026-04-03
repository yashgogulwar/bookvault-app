package com.bookvault.controller;


import com.bookvault.dto.response.BookResponse;
import com.bookvault.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllBooks_returnsOk() throws Exception {
        BookResponse book = BookResponse.builder()
                .id(UUID.randomUUID())
                .title("1984")
                .author("George Orwell")
                .isbn("978-0451524935")
                .genre("Dystopian")
                .totalCopies(2)
                .availableCopies(2)
                .build();

        when(bookService.getAllBooks(null, null, null)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("1984"))
                .andExpect(jsonPath("$.data[0].author").value("George Orwell"));
    }
}
