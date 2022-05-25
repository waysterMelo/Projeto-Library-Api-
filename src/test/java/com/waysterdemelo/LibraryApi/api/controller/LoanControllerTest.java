package com.waysterdemelo.LibraryApi.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysterdemelo.LibraryApi.api.dto.LoanDto;
import com.waysterdemelo.LibraryApi.api.dto.LoanFilterDto;
import com.waysterdemelo.LibraryApi.api.dto.ReturnedLoanDto;
import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.service.BookService;
import com.waysterdemelo.LibraryApi.service.LoanService;
import com.waysterdemelo.LibraryApi.service.LoanServiceTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loan_service;


    @Test
    @DisplayName("Deve realizar um emprestimo")
    public void createLoanTest() throws Exception {
        LoanDto loanDto = LoanDto.builder().isbn("001").email("waystermelo@gmail.com").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(loanDto);

        Book book = Book.builder().id(1L).isbn("001").build();

        BDDMockito.given(bookService.getByIsbn("001")).willReturn(Optional.of(book));

        Loan loan = Loan.builder().id(1L).customer("Fulano").book(book).loanDate(LocalDate.now()).build();

        BDDMockito.given(loan_service.save(Mockito.any(Loan.class))).willReturn(loan);



        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(requestBuilder)
                .andExpect(status().isCreated()).andExpect(content().string("1"));
    }


    @Test
    @DisplayName("Deve retornar um livro")
    public void returnBookTest() throws Exception {
        ReturnedLoanDto loanDto = ReturnedLoanDto.builder().returned(true).build();
        Loan loan = Loan.builder().id(1L).build();
        BDDMockito.given(loan_service.getById(Mockito.anyLong())).willReturn(Optional.of(loan));
        String json = new ObjectMapper().writeValueAsString(loanDto);
        mvc.perform(patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());

        Mockito.verify(loan_service, Mockito.times(1)).update(loan);

    }


    @Test
    @DisplayName("Deve retornar um erro quanto tentar devolver um livro inexistente")
    public void returnInexistentBookTest() throws Exception {
        ReturnedLoanDto loanDto = ReturnedLoanDto.builder().returned(true).build();
        BDDMockito.given(loan_service.getById(Mockito.anyLong())).willReturn(Optional.empty());
        String json = new ObjectMapper().writeValueAsString(loanDto);



        mvc.perform(patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve filtrar livros ")
    public void findLoansTest() throws Exception {
        Long id = 11L;
        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);
        Book book = Book.builder().id(1L).isbn("321").build();
        loan.setBook(book);

        BDDMockito.given(loan_service.find(Mockito.any(LoanFilterDto.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0,10), 1));


        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10", book.getIsbn(), loan.getCustomer());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat(queryString)).accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk()).andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }







}
