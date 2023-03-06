package com.example.creditmarket.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class CartSaveRequestDTO {

    @NotBlank
    private String productId;

}
