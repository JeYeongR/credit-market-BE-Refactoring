package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.FavorAddRequestDTO;
import com.example.creditmarket.dto.response.FavorListResponseDTO;

public interface FavorService {

    String toggleFavorite(FavorAddRequestDTO favorAddRequestDTO, String userEmail);

    FavorListResponseDTO selectFavoriteList(int page, String userEmail);

}
