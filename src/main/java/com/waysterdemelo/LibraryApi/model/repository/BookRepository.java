package com.waysterdemelo.LibraryApi.model.repository;

import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository  extends JpaRepository<Book, Long> {


    boolean existsByIsbn(String isbn);
}
