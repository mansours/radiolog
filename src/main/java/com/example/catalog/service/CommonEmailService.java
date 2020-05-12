package com.example.catalog.service;

import com.example.catalog.service.ref.OfflineWebContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

public abstract class CommonEmailService {

    // <editor-fold desc="Settings and Autowires">
    private final String defaultSenderAddress;
    private final String defaultSenderName;
    private final String baseUrl;

    private final JavaMailSender mailSender;
    protected final TemplateEngine templateEngine;
    private final ServletContext servletContext;
    private final ApplicationContext applicationContext;
    private final ConversionService conversionService;

    public CommonEmailService(String baseUrl, String defaultSenderAddress, String defaultSenderName, JavaMailSender mailSender, TemplateEngine templateEngine, ServletContext servletContext, ApplicationContext applicationContext, ConversionService conversionService) {
        this.baseUrl = baseUrl;
        this.defaultSenderAddress = defaultSenderAddress;
        this.defaultSenderName = defaultSenderName;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.servletContext = servletContext;
        this.applicationContext = applicationContext;
        this.conversionService = conversionService;
    }
    //</editor-fold>

    protected OfflineWebContext getOfflineContext() {
        OfflineWebContext ctx = new OfflineWebContext(servletContext, applicationContext, conversionService);
        ctx.setVariable("baseUrl", baseUrl); //Reserved for url creation in emails.
        return ctx;
    }

    protected void sendEmail(final String recipientEmail, final String subject, final String htmlContent) throws MessagingException, UnsupportedEncodingException {
        sendEmail(Collections.singletonList(recipientEmail), Collections.emptyList(), subject, htmlContent, null, null);
    }

    protected void sendEmail(final String recipientEmail, final String subject, final String htmlContent, final String senderName, final String senderAddress) throws MessagingException, UnsupportedEncodingException {
        sendEmail(Collections.singletonList(recipientEmail), Collections.emptyList(), subject, htmlContent, senderName, senderAddress);
    }

    protected void sendEmail(final List<String> toEmail, final List<String> ccEmail, final String subject, final String htmlContent) throws MessagingException, UnsupportedEncodingException {
        sendEmail(toEmail, ccEmail, subject, htmlContent, null, null);
    }

    protected void sendEmail(final List<String> toEmail, final List<String> ccEmail, final String subject, final String htmlContent, final String senderName, final String senderAddress) throws MessagingException, UnsupportedEncodingException {
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(subject);
        message.setText(htmlContent, true);

        message.setFrom(senderAddress != null ? senderAddress : defaultSenderAddress, senderName != null ? senderName : defaultSenderName);
        message.setReplyTo(senderAddress != null ? senderAddress : defaultSenderAddress);
        message.setTo(toEmail.toArray(new String[0]));
        if (ccEmail != null) message.setCc(ccEmail.toArray(String[]::new));

        this.mailSender.send(mimeMessage); // Send email
    }
}
