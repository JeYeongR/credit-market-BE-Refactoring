package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.OrderRequestDTO;
import com.example.creditmarket.dto.request.OrderSaveRequestDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.OrderResponseDTO;
import com.example.creditmarket.dto.response.ProductDetailResponseDTO;
import com.example.creditmarket.dto.response.RecommendResponseDTO;
import com.example.creditmarket.entity.EntityFProduct;
import com.example.creditmarket.entity.EntityOption;
import com.example.creditmarket.entity.EntityOrder;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.*;
import com.example.creditmarket.service.OrderService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final FProductRespository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final FavoriteRepository favoriteRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;

    /**
     * 상품 구매
     */
    public String buyProduct(OrderSaveRequestDTO requestDTO, String userEmail) {
        EntityUser user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다."));

        List<String> productIds = requestDTO.getProductIds();
        List<EntityFProduct> products = productIds.stream()
                .map(productId -> productRepository.findById(productId).orElseThrow(() ->
                        new IllegalArgumentException("해당 상품을 찾을수 없습니다")))
                .collect(Collectors.toList());

        OrderRequestDTO orderRequestDto = new OrderRequestDTO();
        orderRequestDto.setUser(user);
        for (EntityFProduct product : products) {
            orderRequestDto.setFproduct(product);
            orderRepository.save(orderRequestDto.toEntity());
        }

        return "success";
    }

    public OrderListResponseDTO selectOrderList(int page, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_INDEX_ZERO);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("orderId").descending());

        List<EntityOrder> orders = orderRepository.findByUser(user, pageRequest);

        List<OrderResponseDTO> list = new ArrayList<>();
        for (EntityOrder order : orders) {
            OrderResponseDTO dto = OrderResponseDTO.builder()
                    .order(order)
                    .option(optionRepository.findByProductId(order.getFproduct().getFproduct_id()))
                    .build();

            list.add(dto);
        }

        int totalNum = orderRepository.countByUser(user);

        return OrderListResponseDTO.builder()
                .list(list)
                .totalNum(totalNum)
                .build();
    }

    @Override
    public String updateOrder(Long orderId, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        EntityOrder order = orderRepository.findByUserAndOrderId(user, orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setOrderStatus(0);

        orderRepository.save(order);

        return "success";
    }

    /**
     * 상품 상세정보 출력(상품명, 개요, 대상, 한도, 금리, 찜 여부 등의 상세정보 출력)
     */
    public ProductDetailResponseDTO getProductDetail(String id, HttpServletRequest request) {
        EntityFProduct product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다"));
        EntityOption option = optionRepository.findByProductId(id);
        if (getEmailFromToken(request) == null) {
            return new ProductDetailResponseDTO(product, option, false);
        }
        String userEmail = getEmailFromToken(request);
        EntityUser user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다."));
        boolean isFavorite = favoriteRepository.existsByUserAndFproduct(user, product);
        return new ProductDetailResponseDTO(product, option, isFavorite);
    }

    /**
     * 추천 게시글
     */
    public List<RecommendResponseDTO> recommendList(HttpServletRequest request) {
        List<RecommendResponseDTO> list = new ArrayList<>();
        if (getEmailFromToken(request) == null) {
            return null;
        }
        String userEmail = getEmailFromToken(request);
        EntityUser user = userRepository.findById(userEmail).orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다."));
        List<EntityFProduct> products = productRepository.findProductsByUserPref(user.getUserPrefCreditProductTypeName());
        for (EntityFProduct pr : products) {
            EntityOption op = optionRepository.findOptionByProductIdAndType(pr.getFproduct_id(), user.getUserPrefInterestType());
            boolean isFavorite = favoriteRepository.existsByUserAndFproduct(user, pr);
            if (op != null) {
                list.add(new RecommendResponseDTO(pr, op, isFavorite));
            }
        }
        return list;
    }

    public String getEmailFromToken(HttpServletRequest request) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authorization.split(" ")[1].trim();
            ;
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().get("userEmail", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
