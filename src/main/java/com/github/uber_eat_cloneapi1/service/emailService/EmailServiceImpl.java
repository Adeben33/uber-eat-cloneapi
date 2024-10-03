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
        String emailContent = getUberStyleOtpEmailTemplate(name, otp);

        // Set email details
        helper.setFrom("your-email@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Login");
        helper.setText(emailContent, true); // 'true' indicates HTML content

        // Send the email
        mailSender.send(message);
        System.out.println("OTP email sent successfully to " + toEmail);
    }


    private String getUberStyleOtpEmailTemplate(String name, String otp) {
        return "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"text-align:center;\">"
                + "<img src=\"https://upload.wikimedia.org/wikipedia/commons/c/cc/Uber_logo_2018.png\" alt=\"Uber Logo\" width=\"100\" style=\"display:block;margin:auto;\" />"
                + "</div>"
                + "<div style=\"background-color:#f9f9f9;padding:20px;border-radius:10px;\">"
                + "<h1 style=\"color:#000;font-weight:bold;text-align:center\">Your Uber verification code</h1>"
                + "<p style=\"font-size:1.2em;text-align:center\">Hi " + name + ",</p>"
                + "<p style=\"text-align:center\">To finish logging in to your Uber account, enter this verification code:</p>"
                + "<div style=\"background-color:#eee;padding:10px;border-radius:5px;text-align:center;\">"
                + "<h2 style=\"font-size:2em;color:#00466a;margin:0\">" + otp + "</h2>"
                + "</div>"
                + "<p style=\"font-size:1em;text-align:center;margin-top:20px;\">"
                + "Do not share this code with anyone. Uber staff will never ask you for this code."
                + "</p>"
                + "<p style=\"font-size:0.9em;text-align:center;color:#777\">If you didn’t make this request or you need assistance, visit our <a href=\"#\" style=\"color:#00466a;text-decoration:none;\">Help Center</a>.</p>"
                + "</div>"

                // Footer Section
                + "<div style=\"background:#000000;padding:20px;margin-top:30px;border-radius:8px;\">"
                + "<table style=\"width:100%;color:#fff;font-size:0.9em\">"
                + "<tr>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Help Centre</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Terms</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Privacy</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Email Preferences</a></td>"
                + "</tr>"
                + "</table>"
                + "<p style=\"color:#fff;margin-top:20px;\">Uber Rasier Canada Inc.<br />"
                + "121 Bloor Street East Suite #1600,<br />"
                + "Toronto, ON M4W 3M5<br />"
                + "Uber.com</p>"
                + "</div>"

                + "</div>"
                + "</div>";
    }

}
