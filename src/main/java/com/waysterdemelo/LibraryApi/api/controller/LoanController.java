package com.waysterdemelo.LibraryApi.api.controller;

import com.waysterdemelo.LibraryApi.api.dto.LoanDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.service.BookService;
import com.waysterdemelo.LibraryApi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDto dto){
        Book book = bookService.getByIsbn(dto.getIsbn()).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found for passed isbn"));
        Loan loan = Loan.builder()
                .book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

        loan = loanService.save(loan);
        return loan.getId();

    }
}
