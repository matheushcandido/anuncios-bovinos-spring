package insetec.backend.controllers;

import insetec.backend.models.Sms;
import insetec.backend.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public void sendSms(@RequestBody Sms sms) throws Exception {
        try {
            smsService.sendSms(sms);
        } catch (Exception e) {
            throw new Exception("Erro ao enviar SMS.", e);
        }
    }

    @PostMapping("/validate")
    public boolean validateSms(@RequestBody Map<String, String> requestBody) throws Exception {
        try {
            String userId = requestBody.get("userId");
            String code = requestBody.get("code");

            return smsService.validateSms(userId, code);
        } catch (Exception e) {
            throw new Exception("Erro ao validar SMS.", e);
        }
    }
}