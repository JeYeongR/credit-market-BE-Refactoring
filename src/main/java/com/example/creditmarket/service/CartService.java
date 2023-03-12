package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.CartAddRequestDTO;
import com.example.creditmarket.dto.request.CartDelListRequestDTO;
import com.example.creditmarket.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {

    String addCart(CartAddRequestDTO cartAddRequestDTO, String userEmail);

    List<CartResponseDTO> selectCartList(String userEmail);

    String deleteCart(CartDelListRequestDTO cartDelListRequestDTO, String userEmail);
}
