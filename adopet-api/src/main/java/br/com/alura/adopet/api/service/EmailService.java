package br.com.alura.adopet.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	private static final String sender = "adopet@email.com.br";

	public void sendEmail(String recipient, String subject, String message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(sender);
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText(message);

		emailSender.send(email);
	}
}
