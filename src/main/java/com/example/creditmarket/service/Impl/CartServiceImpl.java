package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.CartDeleteRequestDTO;
import com.example.creditmarket.dto.request.AddRequestDTO;
import com.example.creditmarket.dto.response.CartResponseDTO;
import com.example.creditmarket.entity.EntityCart;
import com.example.creditmarket.entity.EntityFProduct;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.CartRepository;
import com.example.creditmarket.repository.FProductRespository;
import com.example.creditmarket.repository.FavoriteRepository;
import com.example.creditmarket.repository.UserRepository;
import com.example.creditmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final FProductRespository fProductRespository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public String addCart(AddRequestDTO addRequestDTO, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        EntityFProduct fProduct = fProductRespository.findById(addRequestDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (cartRepository.existsByUserAndFproduct(user, fProduct)) {
            return "isDupl";
        }

        cartRepository.save(EntityCart.builder()
                .user(user)
                .fproduct(fProduct)
                .build());

        return "success";
    }

    @Override
    public List<CartResponseDTO> selectCartList(String userEmail) {
        List<EntityCart> cartList = cartRepository.findByUser_UserEmailOrderByCartIdDesc(userEmail);

        return cartList.stream()
                .map(this::checkedFavorite)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteCart(CartDeleteRequestDTO cartDeleteRequestDTO, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        List<EntityCart> cartList = cartDeleteRequestDTO.toEntity();

        cartList.forEach(cart -> cartRepository.findByUserAndCartId(user, cart.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND)));

        cartRepository.deleteAll(cartList);

        return "success";
    }

    //장바구니에 관심 상품표시를 체크하는 메서드
    private CartResponseDTO checkedFavorite(EntityCart cart) {
        boolean isFavorite = favoriteRepository.existsByUserAndFproduct(cart.getUser(), cart.getFproduct());

        return CartResponseDTO.builder()
                .cart(cart)
                .favorite(isFavorite)
                .build();
    }
}
