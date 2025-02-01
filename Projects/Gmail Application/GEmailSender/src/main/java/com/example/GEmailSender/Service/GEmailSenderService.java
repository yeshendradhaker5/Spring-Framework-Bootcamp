package com.example.GEmailSender.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.GEmailSender.Model.GEmailSender;

@Service
public class GEmailSenderService {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	public void sendMail(GEmailSender gEmailSender) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(gEmailSender.getTo());
		message.setSubject(gEmailSender.getSubject());
		message.setText(gEmailSender.getMessage());
		
		javaMailSender.send(message);
	}
}
