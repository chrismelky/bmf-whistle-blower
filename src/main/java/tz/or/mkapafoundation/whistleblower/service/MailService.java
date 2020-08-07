package tz.or.mkapafoundation.whistleblower.service;

import io.github.jhipster.config.JHipsterProperties;
import liquibase.pro.packaged.fo;
import liquibase.pro.packaged.in;
import tz.or.mkapafoundation.whistleblower.domain.Attachment;
import tz.or.mkapafoundation.whistleblower.domain.Notification;
import tz.or.mkapafoundation.whistleblower.domain.User;
import tz.or.mkapafoundation.whistleblower.repository.NotificationRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final AttachmentStore attachmentStore;

    private final NotificationRepository notificationRepository;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine, AttachmentStore attachmentStore,
            NotificationRepository notificationRepository) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.attachmentStore = attachmentStore;
        this.notificationRepository = notificationRepository;
    }

    @Async
    public void sendNotificationEmail(Notification notification) {

        log.info("Sending email notification with", notification);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String to = notification.getEmail();
        try {

            boolean isMultiPart = (notification.getComplain() != null
                    && notification.getComplain().getAttachments().size() > 0);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultiPart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(notification.getSubject());
            message.setText(notification.getComplain().getDescription(), true);

            if (isMultiPart) {
                for (Attachment a : notification.getComplain().getAttachments()) {
                    InputStreamResource inputStreamResource = new InputStreamResource(attachmentStore.getContent(a));
                    String fileName = (a.getName() != null ? a.getName() : "attachment").concat(".pdf");
                    try {
                        message.addAttachment(fileName,
                         new ByteArrayResource(IOUtils.toByteArray(inputStreamResource.getInputStream())));

                    } catch (MessagingException e) {
                        notification.setLog(e.getMessage());
                        notificationRepository.save(notification);
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        notification.setLog(e.getMessage());
                        notificationRepository.save(notification);
                        e.printStackTrace();
                    } catch (IOException e) {
                        notification.setLog(e.getMessage());
                        notificationRepository.save(notification);
                        e.printStackTrace();
                    }
                }
            }
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
            notification.setIsSent(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error("Emailiiii**************", to, e.getMessage());
            notification.setLog(e.getMessage());
            notificationRepository.save(notification);
            if (e.getClass() == MessagingException.class) {
                notification.setIgnore(true);
                notificationRepository.save(notification);       
            }
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
                isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
