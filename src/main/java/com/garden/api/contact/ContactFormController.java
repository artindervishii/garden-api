package com.garden.api.contact;

import com.garden.api.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ContactFormController {

    public static final String BASE_PATH_V1 = "/v1/contact";

    private EmailService emailService;

    @PostMapping(BASE_PATH_V1)
    public ResponseEntity<?> submitContactForm(@RequestBody ContactFormRequest request) {
        emailService.sendContactFormEmail(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getCategoryId(),
                request.getMessage()
        );
        return ResponseEntity.ok("Form submitted successfully");
    }

}
