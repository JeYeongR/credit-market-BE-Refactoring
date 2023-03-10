package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.AddRequestDTO;
import com.example.creditmarket.dto.response.FavoriteListResponseDTO;
import com.example.creditmarket.dto.response.FavoriteResponseDTO;
import com.example.creditmarket.entity.EntityFProduct;
import com.example.creditmarket.entity.EntityFavorite;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.FProductRespository;
import com.example.creditmarket.repository.FavoriteRepository;
import com.example.creditmarket.repository.OptionRepository;
import com.example.creditmarket.repository.UserRepository;
import com.example.creditmarket.service.FavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavorServiceImpl implements FavorService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final OptionRepository optionRepository;
    private final FProductRespository productRepository;

    @Override
    public String toggleFavorite(AddRequestDTO addRequestDTO, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND));

        EntityFProduct product = productRepository.findById(addRequestDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        EntityFavorite favorite = favoriteRepository.findByUserAndFproduct(user, product);
        if (favorite == null) {
            favoriteRepository.save(EntityFavorite.builder()
                    .user(user)
                    .fproduct(product)
                    .build());
        } else {
            favoriteRepository.delete(favorite);
        }

        return "success";
    }

    @Override
    public FavoriteListResponseDTO selectFavoriteList(int page, String userEmail) {
        final int PAGE_SIZE = 10;

        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_INDEX_ZERO);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("favoriteId").descending());

        List<EntityFavorite> favorites = favoriteRepository.findByUser_UserEmail(userEmail, pageRequest);

        List<FavoriteResponseDTO> responseDTOList = favorites.stream()
                .map(favorite -> FavoriteResponseDTO.builder()
                        .favorite(favorite)
                        .option(optionRepository.findByProductId(favorite.getFproduct().getFproduct_id()))
                        .build())
                .collect(Collectors.toList());

        int totalNum = favoriteRepository.countByUser_UserEmail(userEmail);

        return FavoriteListResponseDTO.builder()
                .list(responseDTOList)
                .totalNum(totalNum)
                .build();
    }
}
