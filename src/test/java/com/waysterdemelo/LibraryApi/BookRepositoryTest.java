package com.waysterdemelo.LibraryApi;

import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        String isbn = "001";
        Book book = createNewBook();
        testEntityManager.persist(book);

        boolean exists = bookRepository.existsByIsbn(isbn);
        assertThat(exists).isTrue();
    }

    private Book createNewBook() {
        return Book.builder().title("java").isbn("001").author("wayster").build();
    }

    @Test
    @DisplayName("Deve retornar false quando nao existir um livro com o isbn informado")
    public void returnFalseWhenIsbnExists(){
        String isbn = "001";
        boolean exists = bookRepository.existsByIsbn(isbn);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("should get book by id")
    public void findByIdTest(){
        Book book = createNewBook();
        testEntityManager.persist(book);

        Optional<Book> foundBook =  bookRepository.findById(book.getId());

        assertThat( foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createNewBook();
        Book savedBook = bookRepository.save(book);
        assertThat(savedBook).isNotNull();
    }

    @Test
    @DisplayName("deve deletar um livro")
    public void deleteBookTest(){
        Book book = createNewBook();
        testEntityManager.persist(book);
        Book bookFound = testEntityManager.find(Book.class, book.getId());

        bookRepository.delete(bookFound);

        Book deletedBook =  testEntityManager.find(Book.class, book.getId());

        assertThat(deletedBook).isNull();

    }





}
