package com.waysterdemelo.LibraryApi.service;


import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.repository.BookRepository;
import com.waysterdemelo.LibraryApi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    private Book createValidBook() {
        return Book.builder().isbn("001").author("wayster").title("java").build();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        when(repository.save(book)).thenReturn(
                Book.builder().id(200)
                        .isbn("001")
                        .author("wayster")
                        .title("java").build()
        );

        //execucao
        Book savedBook = service.save(book);

        //verificacao
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("001");
        assertThat(savedBook.getTitle()).isEqualTo("java");
        assertThat(savedBook.getAuthor()).isEqualTo("wayster");
    }

    @Test
    @DisplayName("Deve lancar um erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveBookWithDuplicatedIsbn(){
        //cenario
       Book book =  createValidBook();
       Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

       //exe
       Throwable exceThrowable =  Assertions.catchThrowable( () -> service.save(book));

       //veri
       assertThat(exceThrowable).isInstanceOf(BussinessException.class).hasMessage("Isbn ja cadastrada");
        Mockito.verify(repository, Mockito.never()).save(book);



    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getIdByTest() {
        Long id = Long.valueOf(1);
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execucao
        Optional<Book> foundBook = service.getById(id);

        //verify
        assertThat( foundBook.isPresent()).isTrue();
        assertThat( foundBook.get().getId()).isEqualTo(id);
        assertThat( foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat( foundBook.get().getIsbn()).isEqualTo(book.getIsbn());

    }


    @Test
    @DisplayName("return error with book's id invalid")
    public void getBookNotFoundTest() {
        Long id = Long.valueOf(1);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Book> foundBook = service.getById(id);

        //verify
        assertThat( foundBook.isPresent()).isFalse();

    }


}
