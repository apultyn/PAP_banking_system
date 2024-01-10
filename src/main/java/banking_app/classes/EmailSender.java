package banking_app.classes;

import connections.ConnectionManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

public class EmailSender {
    private static final String FROM = "druzynagpt4@gmail.com";
    private static final String HOST = "smtp.gmail.com";
    private static final String PASSWORD = "tqht nlud qzai fazm";
    private static final Properties properties = System.getProperties();
    private final ConnectionManager manager;

    public EmailSender(ConnectionManager manager) {
        this.manager = manager;
    }

    static {
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }
    public void sendResetCode(String target, String code) throws MessagingException {
        try {
            MimeMessage message = getMimeMessage(target, getSession());
            message.setSubject("Reset code for DruzynaBank");
            message.setText("This is your reset code: " + code);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void sendTransactionInfo(Transaction transaction) {
        try {
            User target = manager.findUserFromAccount(transaction.getTargetId());
            User source = manager.findUserFromAccount(transaction.getSourceId());
            MimeMessage message = getMimeMessage(target.getEmail(), getSession());
            message.setSubject("You received new transaction!");
            String text = String.format("Title: %s\n", transaction.getTitle()) +
                    String.format("From: %s %s\n", source.getName(), source.getSurname()) +
                    String.format("Amount: %s\n", transaction.getAmount().toString()) +
                    String.format("Source account number: %d\n", transaction.getSourceId()) +
                    String.format("Target account number: %d\n", transaction.getTargetId()) +
                    String.format("Date: %s", transaction.getDate().toString());
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage getMimeMessage(String target, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(target));
        return message;
    }

    private Session getSession() {
        return Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }

        });
    }
}
