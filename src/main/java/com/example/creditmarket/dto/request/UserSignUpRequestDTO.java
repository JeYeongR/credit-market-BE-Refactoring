package com.example.creditmarket.dto.request;

import com.example.creditmarket.entity.EntityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class UserSignUpRequestDTO {


    @NotBlank
    @Email
    private String userEmail;
    @NotBlank
    private String userPassword;
    @NotBlank
    private String userName;
    @NotBlank //이걸 그냥 enum으로 하는게 좋지 않나?
    private String userGender;
    @NotBlank //dateTime으로 받는게 좋지 않나?
    private String userBirthDate;
    @NotBlank //얘도 내 기억으로는 선택지에서 고르는거라 enum이 좋다고 생각함
    private String userJob;
    @NotBlank
    private String userPrefCreditProductTypeName;
    @NotBlank
    private String userPrefInterestType;
    @NotBlank
    private Long userCreditScore;

    public EntityUser toEntity(){
        return EntityUser.builder()
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userName(userName)
                .userGender(userGender)
                .userBirthdate(userBirthDate)
                .userJob(userJob)
                .userPrefCreditProductTypeName(userPrefCreditProductTypeName)
                .userPrefInterestType(userPrefInterestType)
                .userCreditScore(userCreditScore)
                .build();
    }

}
