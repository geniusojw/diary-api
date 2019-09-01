package org.jerrioh.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	private static final String USERNAME = "jerrioh.today.diary@gmail.com";
	private static final String PASSWORD = "#today85dia";
	private static final String PERSONAL = "TODAY DIARY";

	// https://myaccount.google.com/security
	private static final String MAIL_APP_ID = "jerrioh.today.diary";
	private static final String MAIL_APP_PASSWORD = "pdqdhdhfgmraixic";
	
	private static final Properties PROPS;
	

	static {
		PROPS = new Properties();
		PROPS.put("mail.smtp.user", USERNAME);
		PROPS.put("mail.smtp.password", PASSWORD);
		PROPS.put("mail.smtp.starttls.enable", "true");
		PROPS.put("mail.smtp.host", "smtp.gmail.com");
		PROPS.put("mail.smtp.auth", "true");
		PROPS.put("mail.smtp.port", "587");
		
//		PROPS.put("mail.debug", "true");
	}

	public static void sendmail(String toMail, String subject, String text) throws MessagingException, UnsupportedEncodingException {
		Session session = Session.getInstance(PROPS, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MAIL_APP_ID, MAIL_APP_PASSWORD);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(USERNAME, PERSONAL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
		message.setSubject(subject);
		message.setText(text);
		
		Transport.send(message);
	}
}
