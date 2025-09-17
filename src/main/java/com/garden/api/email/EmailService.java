package com.garden.api.email;

import com.garden.api.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSenderImpl mailSender;


    public void send(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(EmailDetails.getFrom());
            message.setTo(emailDetails.getTo());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getText());
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordEmail(User user) throws MessagingException {

        String urlToRedirect = "https://dev.jomuntu.com/password/reset?token=" + user.getResetPasswordToken();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String subject = "Rivendosni fjalëkalimin tuaj";
        String htmlContent = "<p>Përshëndetje,</p>" +
                "<p>Ndiq këtë lidhje për të rivendosur fjalëkalimin për llogarinë tënde:</p>" +
                "<p><a href=\"" + urlToRedirect + "\">Kliko këtu</a></p>" +
                "<p>Nëse nuk e ke kërkuar ti rivendosjen e fjalëkalimit, mund ta shpërfillësh këtë email.</p>" +
                "<p>Faleminderit!</p>";

        helper.setFrom("jomuntuapp@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendContactFormEmail(String name, String email, String phone, String service, String messageContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = String.format("New Contact Form Submission from %s", name);

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #2E86C1;">New Contact Form Submission</h2>
                <p><strong>Name:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Phone:</strong> %s</p>
                <p><strong>Service Interested In:</strong> %s</p>
                <p><strong>Message:</strong><br/>%s</p>
            </body>
            </html>
        """.formatted(
                    name != null ? name : "N/A",
                    email != null ? email : "N/A",
                    (phone != null && !phone.isEmpty()) ? phone : "N/A",
                    service != null ? service : "N/A",
                    messageContent != null ? messageContent : "N/A"
            );

            helper.setFrom("no-reply@er-de.de");
            helper.setTo("artini88@outlook.com");
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            System.out.println("Contact form email sent successfully.");
        } catch (MailException | MessagingException e) {
            System.err.println("Failed to send contact form email: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
