package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.OrderAddListRequestDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.ProdDetailResponseDTO;
import com.example.creditmarket.dto.response.RecListResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    ProductDetailResponseDTO getProductDetail(String productId, HttpServletRequest request);

    String buyProduct(OrderAddListRequestDTO orderAddListRequestDTO, String userEmail);

    List<RecommendResponseDTO> recommendList(HttpServletRequest request);

    OrderListResponseDTO selectOrderList(int page, String userEmail);

    String updateOrder(Long orderId, String userEmail);
}
