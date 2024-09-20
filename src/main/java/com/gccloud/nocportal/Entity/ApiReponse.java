package com.gccloud.nocportal.Entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiReponse<T> {

    private String message;
    private Integer statusCode;
    private String flag;
    private T data;

}
