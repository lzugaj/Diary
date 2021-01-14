package com.luv2code.diary.service.impl;

import com.luv2code.diary.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private static final String MAIL_FROM = "luka.zugaj7@gmail.com";

    private static final String USER_STATUS_CHANGED_SUBJECT = "Your status has been changed";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(final String to, final String message) {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(MAIL_FROM);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(USER_STATUS_CHANGED_SUBJECT);
            mimeMessageHelper.setText(message);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            LOGGER.error("Error while sending mail to: ´{}´", to);
            e.printStackTrace();
        }
    }
}
