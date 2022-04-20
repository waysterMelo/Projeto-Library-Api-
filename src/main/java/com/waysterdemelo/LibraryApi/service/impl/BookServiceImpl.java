package com.waysterdemelo.LibraryApi.service.impl;

import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.repository.BookRepository;
import com.waysterdemelo.LibraryApi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())){
            throw new BussinessException("Isbn ja cadastrada");
        }
       return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(long id) {
       return bookRepository.findById(id);
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public Book update(Book book) {
        return null;
    }
}
