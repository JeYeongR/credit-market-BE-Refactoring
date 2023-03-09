package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.AddRequestDTO;
import com.example.creditmarket.dto.response.FavoriteListResponseDTO;

public interface FavorService {

    String toggleFavorite(AddRequestDTO addRequestDTO, String userEmail);

    FavoriteListResponseDTO selectFavoriteList(int page, String userEmail);

}
