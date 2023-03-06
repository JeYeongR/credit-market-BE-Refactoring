package com.example.creditmarket.dto.request;

import com.example.creditmarket.entity.EntityCart;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartDeleteRequestDTO {

    @NotNull
    private List<Long> cartIds;

    public List<EntityCart> toEntity() {
        return cartIds.stream()
                .map(cartId -> EntityCart.builder()
                        .cartId(cartId)
                        .build())
                .collect(Collectors.toList());
    }
}
