package com.garden.api.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EmailDetails {

    @Getter
    private static final String from = "jomuntuapp@gmail.com";
    private String to;
    private String subject;
    private String text;

}

