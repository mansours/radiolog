package com.example.catalog.service;

import com.example.catalog.service.ref.OfflineWebContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class EmailService extends CommonEmailService {

    // <editor-fold desc="Settings and Autowires">
    @Autowired
    public EmailService(@Value("${app.baseurl}") String baseUrl, @Value("${app.mail.sender.email}") String defaultSenderAddress, @Value("${app.mail.sender.name}") String defaultSenderName, JavaMailSender mailSender, TemplateEngine templateEngine, ServletContext servletContext, ApplicationContext applicationContext, ConversionService conversionService) {
        super(baseUrl, defaultSenderAddress, defaultSenderName, mailSender, templateEngine, servletContext, applicationContext, conversionService);
    }
    //</editor-fold>

    public void sendHelloWorldEmail(final String helloName, final String helloMessage, final String helloEmail) throws MessagingException, UnsupportedEncodingException {
        final OfflineWebContext ctx = getOfflineContext();

        //Used for generating hyperlinks in the email body
        ctx.setVariable("helloName", helloName);
        ctx.setVariable("helloMessage", helloMessage);
        //"I'm alive, and I will find you one day."

        final String htmlContent = this.templateEngine.process("mail/helloInbox", ctx);
        sendEmail(helloEmail, "Hello world!", htmlContent);
    }
}
