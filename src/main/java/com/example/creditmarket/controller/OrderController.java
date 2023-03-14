package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.OrderAddListRequestDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.ProdDetailResponseDTO;
import com.example.creditmarket.dto.response.RecListResponseDTO;
import com.example.creditmarket.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> buyProduct(@RequestBody @Valid OrderAddListRequestDTO orderAddListRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        String result = orderService.buyProduct(orderAddListRequestDTO, userEmail);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/order/{page}")
    public ResponseEntity<OrderListResponseDTO> selectOrderList(@PathVariable int page, Authentication authentication) {
        String userEmail = authentication.getName();
        OrderListResponseDTO orderList = orderService.selectOrderList(page, userEmail);

        return ResponseEntity.ok(orderList);
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
