package com.example.GEmailReceiver.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.GEmailReceiver.Entity.Email;

import jakarta.annotation.PostConstruct;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;

@Service
public class GEmailReceiverService {

	private List<Email> allEmailsList = new ArrayList<Email>();
	private int lastMessgeCount = -1;
	private boolean initialFetch = false;

	@PostConstruct
	public void initialMails() {
		fetchAllMails();
		initialFetch = true;
	}

	@Scheduled(fixedRate = 10000) //10 seconds 
	public void newlyReceivedMails() {

		if (!initialFetch) {
			initialMails();
			return;
		}

		try {

			Properties props = new Properties();
			props.put("mail.store.protocol", "imap");
			props.put("mail.imap.host", "imap.gmail.com");
			props.put("mail.imap.port", "993");
			props.put("mail.imap.ssl.enable", "true");

			Session session = Session.getInstance(props);
			Store store = session.getStore("imap");
			store.connect("imap.gmail.com", "yeshendra33@gmail.com", "nmal gstp femv ykpo");

			Folder inboxFolder = store.getFolder("INBOX");
			inboxFolder.open(Folder.READ_ONLY);
			

			int currentMailsCount = inboxFolder.getMessageCount();

			if (currentMailsCount > lastMessgeCount) {
				Message[] newMessages = inboxFolder.getMessages(lastMessgeCount + 1, currentMailsCount);
				System.out.println("New Mails Count " + newMessages.length);

				for (Message message : newMessages) {
					Email email = new Email();
					email.setFrom(InternetAddress.toString(message.getFrom()));
					email.setSubject(message.getSubject());
					email.setReceivedDate(message.getReceivedDate());
					email.setContent(getTextFromMessage(message));

					allEmailsList.add(0, email);
				}
				
				lastMessgeCount = currentMailsCount;

			}
			else {
				System.out.println("No New Mails Found");
			}
			
			inboxFolder.close(false);
			store.close();

		} catch (Exception e) {
			
			System.out.println("Error in fetching newly received mails "+e.getMessage());
		}

	}

	private void fetchAllMails() {

		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imap");
			props.put("mail.imap.host", "imap.gmail.com");
			props.put("mail.imap.port", "993");
			props.put("mail.imap.ssl.enable", "true");

			Session session = Session.getInstance(props);
			Store store = session.getStore("imap");
			store.connect("imap.gmail.com", "yeshendra33@gmail.com", "nmal gstp femv ykpo");

			Folder inboxFolder = store.getFolder("INBOX");
			inboxFolder.open(Folder.READ_ONLY);

			Message[] allMails = inboxFolder.getMessages();
			System.out.println("Initial Fetch total mails count " + allMails.length);
			

			Folder[] folders = store.getDefaultFolder().list("*");
			
			for(Folder folder : folders) {
				System.out.println(folder.getFullName());
			}
			
			for (Message message : allMails) {
				Email email = new Email();
				email.setFrom(InternetAddress.toString(message.getFrom()));
				email.setSubject(message.getSubject());
				email.setReceivedDate(message.getReceivedDate());
				email.setContent(getTextFromMessage(message));

				allEmailsList.add(email);
			}

			lastMessgeCount = inboxFolder.getMessageCount();
			System.out.println("Last Message Count " + lastMessgeCount);

			inboxFolder.close(false);
			store.close();
		}

		catch (Exception e) {
			System.out.println("Error in fetching initial Mails " + e.getMessage());
		}

	}

	public String getTextFromMessage(Message message) throws Exception {

		if (message.isMimeType("text/plain")) {
			return message.getContent().toString(); //OR return message.getContentType().toString();
		}

		else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			return getTextFromMimeMultipart(mimeMultipart);
		}

		return "";

	}

	public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < mimeMultipart.getCount(); i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				builder.append(bodyPart.getContent());
			}
		}
		return builder.toString();
	}

	public List<Email> getEmails() {
		return allEmailsList;
	}
}
