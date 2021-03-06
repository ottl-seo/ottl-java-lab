package codic.ewhain.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
//import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MimeMessage createMessage(String to, String code)throws Exception{
        logger.info(to);
        logger.info(code);

        MimeMessage message = emailSender.createMimeMessage();

        try {
            message.addRecipients(MimeMessage.RecipientType.TO, to); //보내는 대상
            message.setSubject("이화코딕 가입을 위한 인증 코드"); //제목

            String msg = "";
            msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이화인 주소 확인</h1>";
            msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 인증번호를 이화코딕 가입 창에 입력하세요.</p>";
            msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
            msg += code;
            msg += "</td></tr></tbody></table></div>";

            message.setText(msg, "utf-8", "html"); //내용
            message.setFrom(new InternetAddress("dotsizenobia@gmail.com", "ewha-codic")); //보내는 사람
        } catch (UnsupportedEncodingException e) {
            logger.error("Exception raised while sending message to " + to, e);
        }
        return message;
    }

    // 인증코드 만들기
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public void sendSimpleMessage(String to, String code)throws Exception {
        MimeMessage message = createMessage(to, code);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

}
