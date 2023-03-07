package com.example.creditmarket.service;

import com.example.creditmarket.dto.response.FavoriteListResponseDTO;

public interface FavorService {

    String favoriteService(String productId, String userEmail);

    FavoriteListResponseDTO selectFavoriteList(int page, String userEmail);

}
