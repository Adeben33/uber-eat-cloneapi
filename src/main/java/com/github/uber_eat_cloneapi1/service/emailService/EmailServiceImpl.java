package com.github.uber_eat_cloneapi1.service.emailService;


import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Method to send OTP email with HTML template
    public void sendOtpEmail(String toEmail, String name, String accountNumber, String otp) throws MessagingException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Prepare the OTP email template
        String emailContent = getOtpLoginEmailTemplate(name, accountNumber, otp);

        // Set email details
        helper.setFrom("your-email@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Login");
        helper.setText(emailContent, true); // 'true' indicates HTML content

        // Send the email
        mailSender.send(message);
        System.out.println("OTP email sent successfully to " + toEmail);
    }

    // Method that generates the email template
    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
        return "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"border-bottom:1px solid #eee\">"
                + "<a href=\"https://piggybank.netlify.app/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">piggybank</a>"
                + "</div>"
                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
                + "<p style=\"font-size:0.9em;\">Account Number: " + accountNumber + "</p>"
                + "<p>Thank you for choosing OneStopBank. Use the following OTP to complete your Log In procedures. OTP is valid for 5 minutes</p>"
                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>"
                + "<p style=\"font-size:0.9em;\">Regards,<br />OneStopBank</p>"
                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
                + "<p>piggybank Inc</p>"
                + "<p>1600 Amphitheatre Parkway</p>"
                + "<p>California</p>"
                + "</div>"
                + "</div>";
    }
}
