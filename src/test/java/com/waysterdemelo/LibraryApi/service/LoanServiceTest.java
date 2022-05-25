package com.waysterdemelo.LibraryApi.service;

import com.waysterdemelo.LibraryApi.api.dto.LoanFilterDto;
import com.waysterdemelo.LibraryApi.exceptions.BussinessException;
import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.model.repository.LoanRepository;
import com.waysterdemelo.LibraryApi.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    private LoanRepository loanRepository;

    private LoanService serviceLoan;

    @BeforeEach
    public void setUp(){
        this.serviceLoan = new LoanServiceImpl(loanRepository);
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest(){

        String customer = "Fulano";
        Book book = Book.builder()
                .id(1L)
                .build();

        Loan savingLoan  = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .book(Book.builder().id(1L).build())
                .customer("Fulano")
                .build();

        Loan savedLoan  = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .customer(customer)
                .book(book).build();

        when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
        Loan loan = serviceLoan.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());

    }

    @Test
    @DisplayName("Deve lancar erro ao salvar um emprestimo com livro ja cadastrado")
    public void loanedBookSaveTest(){

        String customer = "Fulano";
        Book book = Book.builder()
                .id(1L)
                .build();

        Loan savingLoan  = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .book(Book.builder().id(1L).build())
                .customer("Fulano")
                .build();

        when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);

        Throwable ex = catchThrowable(() -> serviceLoan.save(savingLoan));

        assertThat(ex).isInstanceOf(BussinessException.class).hasMessage("Book already loaned");

        verify(loanRepository, never()).save(savingLoan);



    }

    @Test
    @DisplayName("Get loan info by id")
    public void getLoanDetailsTest(){
        Long id = 1L;

        Loan loan = createLoan();
        loan.setId(id);

        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        Optional<Loan> result = serviceLoan.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId());
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(loanRepository).findById(id);

    }

    @Test
    @DisplayName("Deve atualizar um loan")
    public void updateLoanTest(){
        Loan loan = createLoan();
        loan.setId(1L);
        loan.setReturned(true);

        when(loanRepository.save(loan)).thenReturn(loan);

        Loan updateLoan = serviceLoan.update(loan);
        assertThat(updateLoan.getReturned()).isTrue();
        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar emprestimos pela propriedades")
    public void findSearchLoansTest(){
        LoanFilterDto filterDto = LoanFilterDto.builder().customer("Fulano").isbn("321").build();
        Loan loan = createLoan();
        loan.setId(1L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());

        when(loanRepository.findByBookIsbnOrCustomer(Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Loan> result = serviceLoan.find(filterDto, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }






    public static Loan createLoan(){
        Book book = Book.builder().id(1L).build();
        String customer = "fulano";
        return
                Loan.builder()
                        .book(book).customer(customer).loanDate(LocalDate.now())
                        .build();
    }
}
