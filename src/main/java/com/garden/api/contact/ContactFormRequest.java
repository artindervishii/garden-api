package com.garden.api.contact;

import lombok.Data;

@Data
public class ContactFormRequest {

    String name;
    String email;
    String phone;
    String service;
    String message;

}
