package com.waysterdemelo.LibraryApi.service;

import java.util.List;

public interface EmailService {
    public void sendEmail(String msg, List<String> mailList);
}
