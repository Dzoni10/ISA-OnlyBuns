package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.User;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;
    @Autowired
    private FollowingService followingService;

    @Async
    public void sendVerificationEmail(UserDTO userDTO,String link) throws MailException, InterruptedException, MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        helper.setTo(userDTO.email);
        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setSubject("Verification email");

        String verificationUrl = link;
        String htmlMsg = "<p>Pozdrav " + userDTO.name + ",</p>"
                + "<p>Klikni na link ispod kako bi izvršio verifikaciju:</p>"
                + "<a href='" + verificationUrl + "'>Verifikuj svoj nalog</a>"
                + "<p>Hvala!</p>";

        // Postavite HTML sadržaj mejla
        helper.setText(htmlMsg, true);

        javaMailSender.send(mail);
    }

    public void sendInactivityEmail(User user) throws MailException, InterruptedException, MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(user.getEmail());
        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setSubject("Come back to Onlybuns");
        String htmlMsg = "<p>Pozdrav " + user.getUsername() + ",</p>"
                + "<p>Nisi se ulogovao na onlybuns vec 7 dana. Cekamo te!</p>"
                + "<p>Imas " + followingService.getAllByUserLastActivityCount(user) + " novih pratilaca!</p>";

        helper.setText(htmlMsg, true);

        javaMailSender.send(mail);

        // Simulate sending an email
        System.out.println("Email sent to: " + user.getEmail());
    }
}
