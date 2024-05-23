package com.medxpert.medxpertclient.patient;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String text) throws IOException {
        Email from = new Email("medxpert-hms@outlook.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void sendEmailWithLogo(String to, String subject, String htmlContent) throws IOException, URISyntaxException {
        Email from = new Email("medxpert-hms@outlook.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        // Inline attachments for logo and social media icons
        addInlineAttachment(mail, "/static/images/2.png", "logo", "image/png");
        addInlineAttachment(mail, "/static/images/whatsapp_icon.png", "whatsapp_icon", "image/png");
        addInlineAttachment(mail, "/static/images/facebook_icon.png", "facebook_icon", "image/png");
        addInlineAttachment(mail, "/static/images/instagram_icon.png", "instagram_icon", "image/png");

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

    private void addInlineAttachment(Mail mail, String filePath, String contentId, String mimeType) throws IOException, URISyntaxException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(getClass().getResource(filePath).toURI()));
        String base64File = Base64.getEncoder().encodeToString(fileBytes);

        Attachments attachments = new Attachments();
        attachments.setFilename(filePath.substring(filePath.lastIndexOf("/") + 1));
        attachments.setType(mimeType);
        attachments.setDisposition("inline");
        attachments.setContentId(contentId);
        attachments.setContent(base64File);
        mail.addAttachments(attachments);
    }
}
