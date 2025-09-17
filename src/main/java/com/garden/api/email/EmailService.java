//package com.garden.api.email;
//
//import com.garden.api.user.User;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class EmailService {
//
//    private final JavaMailSenderImpl mailSender;
//
//
//    public void send(EmailDetails emailDetails) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(EmailDetails.getFrom());
//            message.setTo(emailDetails.getTo());
//            message.setSubject(emailDetails.getSubject());
//            message.setText(emailDetails.getText());
//            mailSender.send(message);
//        } catch (MailException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendVerificationEmail(User user) {
//        try {
//            String token = UUID.randomUUID().toString();
//            user.setEmailToken(token);
//
//            String subject = "Verifikoni email-in tuaj";
//            String text = "I nderuar klient,\n\n" +
//                    "Faleminderit që u regjistruat në Jomuntu! Për të përfunduar procesin e regjistrimit dhe për të aktivizuar llogarinë tuaj, ju lutemi të verifikoni email-in duke klikuar në linkun e mëposhtëm:\n" +
//                    token + "\n\n" +
//                    "Nëse nuk e keni kërkuar këtë veprim, ju lutemi ta injoroni këtë email.\n\n" +
//                    "Faleminderit që zgjodhët Jomuntu!\n\n" +
//                    "Me respekt,\n" +
//                    "Ekipi Jomuntu";
//
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setText(text);
//            mailMessage.setSubject(subject);
//            mailMessage.setTo(user.getEmail());
//            mailSender.send(mailMessage);
//
//
//        } catch (MailException e) {
//            e.getMessage();
//        }
//    }
//
//    public void sendForgotPasswordEmail(User user) throws MessagingException {
//
//        String urlToRedirect = "https://dev.jomuntu.com/password/reset?token=" + user.getResetPasswordToken();
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//        String subject = "Rivendosni fjalëkalimin tuaj";
//        String htmlContent = "<p>Përshëndetje,</p>" +
//                "<p>Ndiq këtë lidhje për të rivendosur fjalëkalimin për llogarinë tënde:</p>" +
//                "<p><a href=\"" + urlToRedirect + "\">Kliko këtu</a></p>" +
//                "<p>Nëse nuk e ke kërkuar ti rivendosjen e fjalëkalimit, mund ta shpërfillësh këtë email.</p>" +
//                "<p>Faleminderit!</p>";
//
//        helper.setFrom("jomuntuapp@gmail.com");
//        helper.setTo(user.getEmail());
//        helper.setSubject(subject);
//        helper.setText(htmlContent, true);
//
//        mailSender.send(mimeMessage);
//    }
//
//}
