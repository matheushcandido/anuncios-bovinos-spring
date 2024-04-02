package insetec.backend.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String twilioAccountSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.fromNumber}")
    private String twilioFromNumber;

    public void sendSms(String toNumber, String messageBody) {
        Twilio.init(twilioAccountSid, twilioAuthToken);

        Message message = Message.creator(
                        new PhoneNumber(toNumber),
                        new PhoneNumber(twilioFromNumber),
                        messageBody)
                .create();

        System.out.println("SMS enviado com sucesso! SID: " + message.getSid());
    }
}
