package com.waysterdemelo.LibraryApi.api.exceptions;

import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(BussinessException ex){
        this.errors = Arrays.asList(ex.getMessage());
    }

    public ApiErrors(ResponseStatusException ex){
        this.errors = Arrays.asList(ex.getReason());
    }

    public List<String> getErrors(){
        return errors;
    }

}
