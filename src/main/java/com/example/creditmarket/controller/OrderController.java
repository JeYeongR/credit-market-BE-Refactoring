package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.OrderSaveRequestDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.ProductDetailResponseDTO;
import com.example.creditmarket.dto.response.RecommendResponseDTO;
import com.example.creditmarket.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"상품 서비스"}, description = "상품 상세페이지, 주문, 추천 상품, 상품 찜하기 기능을 담당합니다.")
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "상품 구매", notes = "상품을 구매(신청)합니다.")
    @PostMapping("/order")
    public String buyProduct(@RequestBody OrderSaveRequestDTO requestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        return orderService.buyProduct(requestDTO, userEmail);
    }

    @GetMapping("/order/{page}")
    public ResponseEntity<OrderListResponseDTO> selectOrderList(@PathVariable int page, Authentication authentication) {
        OrderListResponseDTO orderList = orderService.selectOrderList(page, authentication.getName());

        return ResponseEntity.ok().body(orderList);
    }

    @PatchMapping("/order/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable Long orderId, Authentication authentication) {
        String result = orderService.updateOrder(orderId, authentication.getName());

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "상품 상세페이지", notes = "상품의 상세페이지를 보여줍니다.")
    @GetMapping("/item/{id}")
    public ProductDetailResponseDTO itemDetail(@PathVariable String id, HttpServletRequest request) {
        return orderService.getProductDetail(id, request);
    }

    @ApiOperation(value = "상품 추천", notes = "회원의 선호하는 대출상품과 금리에 따라 상품을 추천해줍니다")
    @GetMapping("/recommend")
    public List<RecommendResponseDTO> recommendList(HttpServletRequest request) {
        return orderService.recommendList(request);
    }

}
