package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.CartDeleteRequestDTO;
import com.example.creditmarket.dto.request.CartSaveRequestDTO;
import com.example.creditmarket.dto.response.CartResponseDTO;
import com.example.creditmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addCart(@Valid @RequestBody CartSaveRequestDTO cartRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = cartService.addCart(cartRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> selectCartList(Authentication authentication) {
        String userEmail = authentication.getName();
        List<CartResponseDTO> cartList = cartService.selectCartList(userEmail);

        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@Valid @RequestBody CartDeleteRequestDTO cartDeleteRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = cartService.deleteCart(cartDeleteRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }
}
