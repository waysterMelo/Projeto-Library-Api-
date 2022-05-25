package com.waysterdemelo.LibraryApi.api.controller;

import com.waysterdemelo.LibraryApi.api.dto.BookDto;
import com.waysterdemelo.LibraryApi.api.dto.LoanDto;
import com.waysterdemelo.LibraryApi.api.dto.LoanFilterDto;
import com.waysterdemelo.LibraryApi.api.dto.ReturnedLoanDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.service.BookService;
import com.waysterdemelo.LibraryApi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final ModelMapper modelMapper;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long create(@RequestBody LoanDto dto){
        Book book = bookService.getByIsbn(dto.getIsbn()).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found for passed isbn"));
        Loan loan = Loan.builder()
                .book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

        loan = loanService.save(loan);
        return loan.getId();

    }


    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDto loanDto){
        Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(loanDto.getReturned());
        loanService.update(loan);

    }

    @GetMapping
    public Page<LoanDto> find(LoanFilterDto filterDto, Pageable pageable){
        Page<Loan> result = loanService.find(filterDto, pageable);
        List<LoanDto> loans =  result.getContent().stream().map(entity -> {
                Book book = entity.getBook();
            BookDto bookDto = modelMapper.map(book, BookDto.class);
            LoanDto loanDto = modelMapper.map(entity, LoanDto.class);
            loanDto.setBookDto(bookDto);
            return loanDto; }).collect(Collectors.toList());
        return new PageImpl<LoanDto>(loans, pageable, result.getTotalElements());
    }


}
