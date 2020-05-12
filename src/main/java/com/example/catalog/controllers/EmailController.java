package com.example.catalog.controllers;

import com.example.catalog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/sendEmail")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public String home() {
        return "email";
    }

    @PostMapping
    public String home(@RequestParam String name,
                       @RequestParam String email,
                       @RequestParam String message) throws UnsupportedEncodingException, MessagingException {
        emailService.sendHelloWorldEmail(name, message, email);
        return "redirect:/";
    }
}
