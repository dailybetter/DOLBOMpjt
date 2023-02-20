package com.c103.dolbom.openvidu.service;

import com.c103.dolbom.openvidu.dto.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public int sendEmail(MailDto dto)  {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,"UTF-8");
        try {
            messageHelper.setFrom(from);
            messageHelper.setTo(dto.getEmail());
            messageHelper.setSubject(dto.getTitle());
            StringBuilder sb = new StringBuilder();
            sb.append("회의 링크: "+dto.getLink()+"\n");
            sb.append("참여 코드: " +dto.getSessionId()+"\n");
            sb.append("방 번호: "+dto.getConferenceId());
            messageHelper.setText(sb.toString());
        } catch (MessagingException e) {
            return 0;
        }
        mailSender.send(message);
        return 1;
    }

}
