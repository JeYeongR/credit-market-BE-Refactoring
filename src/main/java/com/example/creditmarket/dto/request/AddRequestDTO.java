package com.example.creditmarket.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class AddRequestDTO {

    @NotBlank
    private String productId;

}
