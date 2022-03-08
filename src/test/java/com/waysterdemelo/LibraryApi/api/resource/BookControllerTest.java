package com.waysterdemelo.LibraryApi.api.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysterdemelo.LibraryApi.api.dto.BookDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    @DisplayName("testar o cadastro do livro")
    public void createBookTest() throws Exception{

        BookDto bookDto = BookDto.builder().author("Author").title("a").isbn("001").build();

        //simular o retorno do service
        Book savedBook = Book.builder().id(2).author("Author").title("a").isbn("001").build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(bookDto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated() )
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("title").value(bookDto.getTitle()))
                .andExpect(jsonPath("author").value(bookDto.getAuthor()))
                .andExpect(jsonPath("isbn").value(bookDto.getIsbn()));

    }

    @Test
    @DisplayName("lancar erro se dados estiverem incompletos.")
    public void createInvalidBookTest(){

    }
}
