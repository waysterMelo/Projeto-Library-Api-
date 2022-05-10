package com.waysterdemelo.LibraryApi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysterdemelo.LibraryApi.api.dto.LoanDto;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.service.BookService;
import com.waysterdemelo.LibraryApi.service.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        LoanDto loanDto = LoanDto.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(loanDto);

        Book book = Book.builder().id(1L).isbn("123").build();

        BDDMockito.given(bookService.getByIsbn("123")).willReturn(Optional.of(book));

        Loan loan = Loan.builder().id(1L).customer("Fulano").book(book).loanDate(LocalDate.now()).build();

        BDDMockito.given(loan_service.save(Mockito.any(Loan.class))).willReturn(loan);



        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L));
    }















}
