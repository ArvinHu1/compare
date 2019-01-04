package com.statestr.arvin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MailUtils {

	private static Logger logger = Logger.getLogger(MailUtils.class);

	private static JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

	public static void main(String[] args) throws AddressException, MessagingException, GeneralSecurityException {

//		 File file = new
//		 File("C:\\Users\\a805370\\workspace\\CompareFile\\build\\dist\\result\\result.txt");
//		 sendWithAttachment(file);
		
		try {
			sendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Map<String, String> map = new HashMap<String, String>();
//		map = getMailInfo();
//		String[] recs = map.get("receiver").split(",");
//		System.out.println(recs.length);

	}

	public static void sendMail() throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		map = getMailInfo();
		String sender = map.get("sender");
		String password = map.get("password");
		String receivers = map.get("receiver");
		String[] receiverlist = receivers.split(",");
		String subject = map.get("subject");

		Properties props = System.getProperties();
		props.setProperty("mail.transport.protocol", "SMTP");
		props.put("mail.smtp.host", "outlooknam.statestr.com");
		props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.auth.mechanisms", "NTLM");
		// props.put("mail.smtp.auth.ntlm.domain", "corp.statestr.com");
		// props.put("mail.imaps.ssl.trust", "*");

		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		if (receiverlist != null && receiverlist.length > 0) {
			for (String rec : receiverlist) {
				if (!"".equals(rec.trim())) {
					message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(rec.trim()));
				}
			}
			message.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));
			message.setContent("Hello, please see attachment of notes.", "text/html;charset=utf-8");
			Transport trans = session.getTransport("smtp");
			trans.connect();
			Transport.send(message);
			trans.close();
			logger.info("mail sent.");
		} else {
			logger.error("receiver can not be empty!");
		}
	}

	public static void sendWithAttachment(String sender, List<String> receivers, String subject, String password, File file) {

//		Map<String, String> map = new HashMap<String, String>();
//		map = getMailInfo();

//		String sender = map.get("sender");
//		String receivers = map.get("receiver");
//		String subject = map.get("subject");
		String content = "Hello, please see attachment of notes.";
//		String[] receiverlist = receivers.split(",");

		javaMailSender.setUsername(sender);
		javaMailSender.setPassword(password);
		javaMailSender.setHost("outlooknam.statestr.com");
		final MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(new InternetAddress(sender));
				for (String rec : receivers) {
					if (!"".equals(rec.trim())) {
						helper.addTo(new InternetAddress(rec));
					}
				}
				helper.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
				helper.setText(content, true);
				helper.addAttachment("result.txt", new FileDataSource(file));
			}
		};
		try {
			javaMailSender.send(preparator);
			logger.info("mail sent with attachment.");
		} catch (MailSendException e) {
			logger.error("Send Mail failed", e);
		}

	}

	public static Map<String, String> getMailInfo() {

		Properties props = new Properties();
		InputStream is = null;

		Map<String, String> map = null;
		try {
			is = new FileInputStream("config/config.properties");
//			props.load(is);
			props.load(new InputStreamReader(is, Charset.forName("UTF-8")));
			String sender = props.getProperty("sender");
			String password = props.getProperty("password");
			String receiver = props.getProperty("receiver");
			String subject = props.getProperty("subject");
			map = new HashMap<String, String>();
			map.put("sender", sender);
			map.put("password", password);
			map.put("receiver", receiver);
			map.put("subject", subject);
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("config.properties not found!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IO Exception!");
		}
		return null;

	}
}
