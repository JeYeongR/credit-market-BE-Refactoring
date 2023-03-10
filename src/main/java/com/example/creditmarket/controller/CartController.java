package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.CartAddRequestDTO;
import com.example.creditmarket.dto.request.CartDelListRequestDTO;
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
    public ResponseEntity<String> addCart(@Valid @RequestBody CartAddRequestDTO cartAddRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = cartService.addCart(cartAddRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> selectCartList(Authentication authentication) {
        String userEmail = authentication.getName();
        List<CartResponseDTO> cartList = cartService.selectCartList(userEmail);

        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@Valid @RequestBody CartDelListRequestDTO cartDelListRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = cartService.deleteCart(cartDelListRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }
}
