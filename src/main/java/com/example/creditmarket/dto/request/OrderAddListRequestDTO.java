package com.example.creditmarket.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderAddListRequestDTO {

    @NotNull
    @Valid
    List<OrderAddRequestDTO> orderAddList;

}
