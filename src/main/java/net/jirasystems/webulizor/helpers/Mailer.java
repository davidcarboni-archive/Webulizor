package net.jirasystems.webulizor.helpers;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import net.jirasystems.resourceutil.ResourceUtil;
import net.jirasystems.webulizor.framework.AppException;

import com.sun.mail.smtp.SMTPMessage;

/**
 * Simple email sender.
 */
public final class Mailer {

	private static Properties fMailServerConfig = new Properties();

	/**
	 * Send email.
	 * 
	 * @param from
	 *            Sender.
	 * @param to
	 *            Recipient.
	 * @param subject
	 *            Message subject.
	 * @param body
	 *            Message text.
	 */
	public void sendEmail(String from, String to, String subject, String body) {
		Session session = Session.getDefaultInstance(fetchConfig(), null);
		SMTPMessage message = new SMTPMessage(session);
		try {
			message.setEnvelopeFrom(from);
			message.setSender(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException ex) {
			throw new AppException("Cannot send email. " + ex);
		}
	}

	/**
	 * Load <code>/mail.properties</code>.
	 */
	private Properties fetchConfig() {
		if (fMailServerConfig == null) {
			String name = "/mail.properties";
			try {
				fMailServerConfig = ResourceUtil
						.getProperties("/mail.properties");
				// Properties result = new Properties(fMailServerConfig);
				// result.putAll(System.getProperties());
				// result.putAll(fMailServerConfig);
				// fMailServerConfig = result;
			} catch (IOException ex) {
				throw new RuntimeException("Cannot open " + name);
			}
		}
		return fMailServerConfig;
	}
}
