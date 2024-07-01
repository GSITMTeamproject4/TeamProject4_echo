package com.project.echoproject.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmailMessage {

    private String to;
    private String subject;
    private String message;

}