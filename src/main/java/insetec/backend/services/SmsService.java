package insetec.backend.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import insetec.backend.models.Sms;
import insetec.backend.models.User;
import insetec.backend.repositories.SmsRepository;
import insetec.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String twilioAccountSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.fromNumber}")
    private String twilioFromNumber;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendSms(Sms sms) {
        smsRepository.save(sms);

        Twilio.init(twilioAccountSid, twilioAuthToken);

        String messageBody = "Seu código de verificação: " + sms.getCode();

        Message message = Message.creator(
                new PhoneNumber(sms.getPhoneNumber()),
                new PhoneNumber(twilioFromNumber),
                messageBody).create();

        System.out.println("SMS enviado com sucesso! SID: " + message.getSid());
    }

    public boolean validateSms(String userId, String code) {
        Sms sms = smsRepository.findByUserId(userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (sms != null && userOptional.isPresent()) {
            User user = userOptional.get();

            if (sms.getCode().equals(code)) {
                user.getContact().setVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}