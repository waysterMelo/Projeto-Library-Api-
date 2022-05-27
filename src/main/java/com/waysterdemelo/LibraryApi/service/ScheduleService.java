package com.waysterdemelo.LibraryApi.service;

import com.waysterdemelo.LibraryApi.model.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {


    @Value("${application.mail.lateloans.message}")
    private String msg;

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private  LoanService loanService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendMailsToPublicLoans(){
        List<Loan> list = loanService.getAllLateLoans();
        List<String> mailList = list.stream().map(loan -> loan.getCustomerEmail()).collect(Collectors.toList());

        emailService.sendEmail(msg, mailList);
    }

}
