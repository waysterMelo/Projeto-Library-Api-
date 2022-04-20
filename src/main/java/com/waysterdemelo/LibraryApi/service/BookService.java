package com.waysterdemelo.LibraryApi.service;

import com.waysterdemelo.LibraryApi.model.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(long id);

    void delete(Book book);

    Book update(Book book);

}
