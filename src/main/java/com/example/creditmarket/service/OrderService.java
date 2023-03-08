package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.OrderSaveRequestDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.ProductDetailResponseDTO;
import com.example.creditmarket.dto.response.RecommendResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    ProductDetailResponseDTO getProductDetail(String productId, HttpServletRequest request);

    String buyProduct(OrderSaveRequestDTO requestDTO, String userEmail);

    List<RecommendResponseDTO> recommendList(HttpServletRequest request);

    OrderListResponseDTO selectOrderList(int page, String userEmail);

    String updateOrder(Long orderId, String userEmail);
}
