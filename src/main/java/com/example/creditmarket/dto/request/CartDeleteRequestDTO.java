package com.example.creditmarket.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartDeleteRequestDTO {

    @NotNull
    private Long cartId;

}
