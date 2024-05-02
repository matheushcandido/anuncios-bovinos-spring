package insetec.backend.controllers;

import insetec.backend.models.User;
import insetec.backend.repositories.UserRepository;
import insetec.backend.services.SmsService;
import insetec.backend.models.Sms;
import insetec.backend.repositories.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send")
    public void sendSms(@RequestBody Sms sms) {

        smsRepository.save(sms);

        String messageBody = "Seu código de verificação: " + sms.getCode();
        smsService.sendSms(sms.getPhoneNumber(), messageBody);
    }

    @PostMapping("/validate")
    public boolean validateSms(@RequestParam String userId, @RequestParam String code) {
        Sms sms = smsRepository.findByUserId(userId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (sms != null && userOptional.isPresent()) {
            User user = userOptional.get();

            if (sms.getCode().equals(code)) {
                user.getContact().setVerified(true);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}