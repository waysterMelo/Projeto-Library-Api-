package com.waysterdemelo.LibraryApi.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysterdemelo.LibraryApi.api.dto.BookDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.repository.BookRepository;
import com.waysterdemelo.LibraryApi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void SetUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = Book.builder().author("wayster").isbn("132133254d").title("Node.js").build();
        Mockito.when( repository.save(book)).thenReturn(Book.builder()
                .id(11)
                .isbn("132133254d")
                .author("wayster")
                        .title("Node.js")
                .build());

        Book savedBook = service.save(book);


        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getAuthor()).isEqualTo("wayster");
        assertThat(savedBook.getIsbn()).isEqualTo("132133254d");
        assertThat(savedBook.getTitle()).isEqualTo("Node.js");
    }


}
