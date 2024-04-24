package insetec.backend.controllers;

import insetec.backend.services.SmsService;
import insetec.backend.models.Sms;
import insetec.backend.repositories.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsRepository smsRepository;

    @PostMapping("/send")
    public void sendSms(@RequestBody Sms sms) {

        smsRepository.save(sms);

        String messageBody = "Seu código de verificação: " + sms.getCode();
        smsService.sendSms(sms.getPhoneNumber(), messageBody);
    }
}