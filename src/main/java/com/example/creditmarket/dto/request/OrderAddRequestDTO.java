package com.example.creditmarket.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderAddRequestDTO {

    List<String> productIds;

}
