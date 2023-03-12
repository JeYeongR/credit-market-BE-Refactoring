package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.FavorAddRequestDTO;
import com.example.creditmarket.dto.response.FavorListResponseDTO;
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
    public ResponseEntity<String> toggleFavorite(@RequestBody FavorAddRequestDTO favorAddRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = favorService.toggleFavorite(favorAddRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/favor/{page}")
    public ResponseEntity<FavorListResponseDTO> selectFavoriteList(@PathVariable int page, Authentication authentication) {
        String userEmail = authentication.getName();
        FavorListResponseDTO favoriteList = favorService.selectFavoriteList(page, userEmail);

        return ResponseEntity.ok(favoriteList);
    }
}
