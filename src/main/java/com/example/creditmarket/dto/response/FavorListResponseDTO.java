package com.example.creditmarket.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FavorListResponseDTO {

    private List<FavorResponseDTO> list;
    private int totalNum;

    @Builder
    public FavorListResponseDTO(List<FavorResponseDTO> list, int totalNum) {
        this.list = list;
        this.totalNum = totalNum;
    }
}
