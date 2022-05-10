package com.waysterdemelo.LibraryApi.service.impl;

import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.repository.BookRepository;
import com.waysterdemelo.LibraryApi.service.BookService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if (book == null || book.getId() == null){
            throw new IllegalArgumentException("Book id can't be null");
        }
        this.bookRepository.delete(book);

    }

    @Override
    public Book update(Book book) {

        if (book == null || book.getId() == null){
            throw new IllegalArgumentException("Book id can't be null");
        }

        return this.bookRepository.save(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageableRequest) {
        Example<Book> example = Example.of(filter, ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return bookRepository.findAll(example, pageableRequest);
    }

    @Override
    public Object getByIsbn(String s) {
        return null;
    }


}
