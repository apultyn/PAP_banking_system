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

    public void sendTransferInfo(Transfer transfer) {
        try {
            User target = manager.findUserFromAccount(transfer.getTargetId());
            User source = manager.findUserFromAccount(transfer.getSourceId());
            MimeMessage message = getMimeMessage(target.getEmail(), getSession());
            message.setSubject("You received new transfer!");
            String text = String.format("Title: %s\n", transfer.getTitle()) +
                    String.format("From: %s %s\n", source.getName(), source.getSurname()) +
                    String.format("Amount: %s\n", transfer.getAmount().toString()) +
                    String.format("Source account number: %d\n", transfer.getSourceId()) +
                    String.format("Target account number: %d\n", transfer.getTargetId()) +
                    String.format("Date: %s", transfer.getDate().toString());
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendVerificationNumber(String number, User user) {
        try {
            MimeMessage message = getMimeMessage(user.getEmail(), getSession());
            message.setSubject("Your verification code.");
            String text = String.format("Your verification code: %s.", number);

    public void sendLoginInfo(User user) {
        try {
            MimeMessage message = getMimeMessage(user.getEmail(), getSession());
            message.setSubject("New logging operation at your account!");
            String text = String.format("Hi: %s\n", user.getName()) +
                    String.format("There was new logging at your DruzynaBank account registered on email: %s\n", user.getEmail()) +
                    "If that's you, don't respond to this message\n" +
                    "Otherwise, contact our Support, someone probably broke onto your account!";

            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
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
