package com.garden.api.contact;

import lombok.Data;

@Data
public class ContactFormRequest {

    String name;
    String email;
    String phone;
    String categoryId;
    String message;

}
