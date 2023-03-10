package com.example.creditmarket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserLoginRequestDTO {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String userPassword;
}
