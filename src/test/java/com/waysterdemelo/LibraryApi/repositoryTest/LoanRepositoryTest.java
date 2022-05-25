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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepositoryTest;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Deve verificar se existe um emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturnedTest(){
        Book book = new Book();
        testEntityManager.persist(book);
        Loan loan =  Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        testEntityManager.persist(loan);

        boolean exists = loanRepositoryTest.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Must search loan by isbn or customer")
    public void findByBookIsbnOrCustomer(){
        createAndPersistLoan();
        Page<Loan> result = loanRepositoryTest.findByBookIsbnOrCustomer("001", "Fulano", PageRequest.of(0, 10));

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    public Loan createAndPersistLoan(){
        Book book = new Book();

        testEntityManager.persist(book);
        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        testEntityManager.persist(loan);
        return loan;
    }

}
