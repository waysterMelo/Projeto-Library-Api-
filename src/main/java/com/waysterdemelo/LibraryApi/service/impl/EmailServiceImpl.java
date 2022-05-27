package com.waysterdemelo.LibraryApi.service.impl;

import com.waysterdemelo.LibraryApi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${application.mail.default-remetent}")
    private  String remetent;


    @Override
    public void sendEmail(String msg, List<String> mailList) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(remetent);
        simpleMailMessage.setSubject("Livro com emprestimo atrasado");
        simpleMailMessage.setText(msg);
        String[] mails = mailList.toArray(new String[mailList.size()]);
        simpleMailMessage.setTo(mails);

        javaMailSender.send(simpleMailMessage);
    }
}
