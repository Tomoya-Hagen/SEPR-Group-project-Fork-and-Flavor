package at.ac.tuwien.sepr.groupphase.backend.service;

/**
 * This is the interface for the emailService.
 */
public interface EmailService {

    /**
     * Sends a simple email to the given email address.
     *
     * @param to the email address to send the email to
     * @param subject the subject of the email
     * @param text the text of the email
     */
    void sendSimpleEmail(String to, String subject, String text);
}
