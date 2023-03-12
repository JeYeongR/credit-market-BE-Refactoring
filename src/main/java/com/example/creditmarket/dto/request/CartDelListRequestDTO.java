package com.example.creditmarket.dto.request;

import com.example.creditmarket.entity.EntityCart;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartDelListRequestDTO {

    @NotNull
    @Valid
    private List<CartDeleteRequestDTO> cartIds;

    public List<EntityCart> toEntity() {
        return cartIds.stream()
                .map(CartDeleteRequestDTO -> EntityCart.builder()
                        .cartId(CartDeleteRequestDTO.getCartId())
                        .build())
                .collect(Collectors.toList());
    }
}
