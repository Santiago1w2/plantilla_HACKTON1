package org.test.week06lab01.mail.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.test.week06lab01.mail.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    public EmailController(final EmailService emailService){
        this.emailService=emailService;

    }
    @PostMapping("/send")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ){
        this.emailService.sendEmail(to,subject,body);
        return "Email send to " + to;
    }
}
