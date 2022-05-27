package com.waysterdemelo.LibraryApi.repositoryTest;

import com.waysterdemelo.LibraryApi.model.entity.Book;
import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.model.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Deve verificar se existe um emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturnedTest(){
        Book book = new Book();
        testEntityManager.persist(book);
        Loan loan =  Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        testEntityManager.persist(loan);

        boolean exists = loanRepository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Must search loan by isbn or customer")
    public void findByBookIsbnOrCustomer(){
        createAndPersistLoan(LocalDate.now());
        Page<Loan> result = loanRepository.findByBookIsbnOrCustomer("001", "Fulano", PageRequest.of(0, 10));

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("get loans less than 3 days and not returned")
    public void findByLoanDateLessThanAndNotReturned(){
        Loan loan  = createAndPersistLoan(LocalDate.now().minusDays(5));
        List<Loan> list = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.contains(loan));

    }

    @Test
    @DisplayName("return null when dont have late loans")
    public void notFindByLoanDateLessThanAndNotReturned(){
        Loan loan  = createAndPersistLoan(LocalDate.now());
        List<Loan> list = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
        assertThat(list.isEmpty());

    }








    public Loan createAndPersistLoan(LocalDate loanDate){
        Book book = new Book();

        testEntityManager.persist(book);
        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(loanDate).build();
        testEntityManager.persist(loan);
        return loan;
    }

}
