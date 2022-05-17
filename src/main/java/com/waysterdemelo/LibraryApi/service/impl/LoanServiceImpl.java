package com.waysterdemelo.LibraryApi.service.impl;

import com.waysterdemelo.LibraryApi.model.entity.Loan;
import com.waysterdemelo.LibraryApi.model.repository.LoanRepository;
import com.waysterdemelo.LibraryApi.service.LoanService;

public class LoanServiceImpl implements LoanService {


    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {

        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }
}
