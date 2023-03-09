package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.AddRequestDTO;
import com.example.creditmarket.dto.request.CartDeleteRequestDTO;
import com.example.creditmarket.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {

    String addCart(AddRequestDTO addRequestDTO, String userEmail);

    List<CartResponseDTO> selectCartList(String userEmail);

    String deleteCart(CartDeleteRequestDTO cartDeleteRequestDTO, String userEmail);
}
