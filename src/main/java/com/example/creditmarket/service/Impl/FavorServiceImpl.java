package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.FavoriteRequestDto;
import com.example.creditmarket.dto.response.FavoriteListResponseDTO;
import com.example.creditmarket.dto.response.FavoriteResponseDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.OrderResponseDTO;
import com.example.creditmarket.entity.EntityFProduct;
import com.example.creditmarket.entity.EntityFavorite;
import com.example.creditmarket.entity.EntityOrder;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.*;
import com.example.creditmarket.service.FavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavorServiceImpl implements FavorService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final OptionRepository optionRepository;
    private final FProductRespository productRepository;

    public String favoriteService(String productId, String userEmail) {
        EntityFProduct product = productRepository.findById(productId).orElseThrow(() ->
                new IllegalArgumentException("해당 상품을 찾을수 없습니다"));
        EntityUser user = userRepository.findById(userEmail).orElseThrow(() ->
                new IllegalArgumentException("해당 회원을 찾을수 없습니다."));
        try {
            EntityFavorite favorite = favoriteRepository.findEntityFavoriteByFproductAndUser(product, user);
            if (favorite == null) {
                FavoriteRequestDto dto = new FavoriteRequestDto();
                favoriteRepository.save(dto.toEntity(user, product));
            } else {
                favoriteRepository.deleteById(favorite.getFavoriteId());
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public FavoriteListResponseDTO selectFavoriteList(int page, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_INDEX_ZERO);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("favoriteId").descending());

        List<EntityFavorite> favorites = favoriteRepository.findByUser(user, pageRequest);

        List<FavoriteResponseDTO> list = new ArrayList<>();
        for (EntityFavorite favorite : favorites) {
            FavoriteResponseDTO dto = FavoriteResponseDTO.builder()
                    .favorite(favorite)
                    .option(optionRepository.findByProductId(favorite.getFproduct().getFproduct_id()))
                    .build();

            list.add(dto);
        }

        int totalNum = favoriteRepository.countByUser(user);

        return FavoriteListResponseDTO.builder()
                .list(list)
                .totalNum(totalNum)
                .build();
    }
}
