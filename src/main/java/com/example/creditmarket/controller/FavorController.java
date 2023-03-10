package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.AddRequestDTO;
import com.example.creditmarket.dto.response.FavoriteListResponseDTO;
import com.example.creditmarket.service.FavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FavorController {

    private final FavorService favorService;

    @PostMapping("/favor")
    public ResponseEntity<String> toggleFavorite(@RequestBody AddRequestDTO addRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = favorService.toggleFavorite(addRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/favor/{page}")
    public ResponseEntity<FavoriteListResponseDTO> selectFavoriteList(@PathVariable int page, Authentication authentication) {
        String userEmail = authentication.getName();
        FavoriteListResponseDTO favoriteList = favorService.selectFavoriteList(page, userEmail);

        return ResponseEntity.ok(favoriteList);
    }
}
