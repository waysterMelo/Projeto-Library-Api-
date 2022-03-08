package com.waysterdemelo.LibraryApi.api.resource;

import com.waysterdemelo.LibraryApi.api.dto.BookDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody BookDto bookDto){

        Book entity = modelMapper.map( bookDto, Book.class);

        entity = service.save(entity);
        return modelMapper.map(entity, BookDto.class);
    }

}
