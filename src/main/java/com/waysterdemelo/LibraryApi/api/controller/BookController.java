package com.waysterdemelo.LibraryApi.api.controller;

import com.waysterdemelo.LibraryApi.api.dto.BookDto;
import com.waysterdemelo.LibraryApi.api.dto.LoanDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.service.BookService;
import com.waysterdemelo.LibraryApi.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("BOOK API")
public class BookController {

    private final BookService service;
    private final ModelMapper modelMapper;
    private  LoanService loanService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("CADASTRAR UM LIVRO")
    public BookDto create(@RequestBody @Valid BookDto bookDto){

        Book entity = modelMapper.map( bookDto, Book.class);

        entity = service.save(entity);
        return modelMapper.map(entity, BookDto.class);
    }


    @GetMapping("{id}")
    public BookDto get(@PathVariable Long id) {
        return service.getById(id).map( book -> modelMapper.map(book, BookDto.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(book);
    }

    @PutMapping("{id}")
    @ApiOperation("ALTERAR UM LIVRO")
    public BookDto update(@PathVariable Long id, BookDto dto){
        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book = service.update(book);
        return modelMapper.map(book, BookDto.class);
    }

    @GetMapping
    @ApiOperation("ACHAR UM LIVRO POR ID")
    public Page<BookDto> find(BookDto bookDto, Pageable pageable){
        Book filter = modelMapper.map(bookDto, Book.class);
        Page<Book> result = service.find(filter, pageable);
        List<BookDto> lista =  result.getContent()
                .stream().map(entity -> modelMapper.map(entity, BookDto.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDto>(lista, pageable, result.getTotalElements());
    }

    @GetMapping("{id}/loans")
    public Page<LoanDto> loansByBook(@PathVariable Long id, Pageable pageable){
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Page<Loan> result = loanService.getLoansByBook(book, pageable);

        List<LoanDto> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanedBook = loan.getBook();
                    BookDto bookDto = modelMapper.map(loanedBook, BookDto.class);
                    LoanDto loanDto = modelMapper.map(loan, LoanDto.class);
                    loanDto.setBookDto(bookDto);
                    return loanDto;
                }).collect(Collectors.toList());
        return new PageImpl<LoanDto>(list, pageable, result.getTotalElements());
    }
}
