package com.example.creditmarket.service;

import com.example.creditmarket.dto.response.FavoriteListResponseDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;

public interface MyPageService {

    FavoriteListResponseDTO selectFavoriteList(int page, String userEmail);

    OrderListResponseDTO selectOrderList(int page, String userEmail);

    String updateOrder(Long orderId, String userEmail);

}
