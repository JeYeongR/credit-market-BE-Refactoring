package com.example.creditmarket.controller;

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

    @PostMapping("/favor/{id}")
    public String addLike(@PathVariable String id, Authentication authentication) {
        String userEmail = authentication.getName();
        return favorService.favoriteService(id, userEmail);
    }

    @GetMapping("/favor/{page}")
    public ResponseEntity<FavoriteListResponseDTO> selectFavoriteList(@PathVariable int page, Authentication authentication) {
        FavoriteListResponseDTO favoriteList = favorService.selectFavoriteList(page, authentication.getName());

        return ResponseEntity.ok().body(favoriteList);
    }

    @DeleteMapping("/favor/{id}")
    public String cancelLike(@PathVariable String id, Authentication authentication) {
        String userEmail = authentication.getName();
        return favorService.favoriteService(id, userEmail);
    }
}
