package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.UserSignUpRequestDTO;
import com.example.creditmarket.dto.response.LoginResponseDTO;
import com.example.creditmarket.entity.EntityToken;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.TokenRepository;
import com.example.creditmarket.repository.UserRepository;
import com.example.creditmarket.service.UserService;
import com.example.creditmarket.utils.JwtUtil;
import com.example.creditmarket.utils.RandomCertNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender mailSender;


    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60 * 24 * 7L; //일주일

    @Override
    public String signup(UserSignUpRequestDTO request) {

        //userEmail 중복 체크
        userRepository.findByUserEmail(request.getUserEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERMAIL_DUPLICATED, "이미 존재하는 이메일입니다.");
                });

        //저장
        //userRepository.save(request.toEntity());
        EntityUser EncodedEntityUser = EntityUser.builder()
                .userEmail(request.getUserEmail())
                .userPassword(encoder.encode(request.getUserPassword())) //Encoded
                .userGender(request.getUserGender())
                .userBirthdate(request.getUserBirthDate())
                .userJob(request.getUserJob())
                .userPrefCreditProductTypeName(request.getUserPrefCreditProductTypeName())
                .userPrefInterestType(request.getUserPrefInterestType())
                .userCreditScore(request.getUserCreditScore())
                .build();
        userRepository.save(EncodedEntityUser);
        return "success";
    }

    @Override
    public LoginResponseDTO login(String userEmail, String password) {
        //userEmail 없음
        EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        //password 틀림
        if (!encoder.matches(password, selectedUser.getUserPassword())) { //순서 중요. inputpassword, DBpassword
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }

        String token = JwtUtil.createToken(selectedUser.getUserEmail(), secretKey, expiredMs);
        return new LoginResponseDTO(selectedUser.getUserName(), token);
    }

    @Override
    public Boolean isValid(String userToken) {
        //userToken 없음
        return tokenRepository.findByToken(userToken) == null;
    }

    @Override
    public String logout(HttpServletRequest request) {
        // userToken 없음
        // Token 꺼내기
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
        tokenRepository.save(new EntityToken(token));
        return "LOGOUT_SUCCESS";
    }

    @Override
    public EntityUser passwordCheck(String userEmail, String password) {
        //userEmail 없음
        EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        //password 틀림
        if (!encoder.matches(password, selectedUser.getUserPassword())) { //순서 중요. inputpassword, DBpassword
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }
        return selectedUser;
    }

    @Override
    public String infoUpdate(EntityUser user) {
        userRepository.save(user);
        return "success";
    }

    @Override
    public EntityUser getUserInfo(HttpServletRequest request) {
        // userToken 없음
        // Token 꺼내기
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
        String userEmail = JwtUtil.getUserEmail(token, secretKey);
        EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));
        return selectedUser;
    }

    @Override
    public String sendEmailAuth(String userEmail) {
        if (!userRepository.existsById(userEmail)) {
            throw new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다.");
        }
        String certNum = RandomCertNumber.getCertNum(5);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail); //받는 사람
        message.setSubject("안녕하세요. Credit Market입니다."); //제목
        message.setText("인증번호는 [" + certNum + "]입니다."); //내용
        message.setFrom("wpdud2003@gmail.com"); //보내는 사람
        log.info("certNum={}", certNum);

        mailSender.send(message);

        return certNum;
    }

    @Override
    public String sendNewPasswordAuth(String userEmail) {
        if (!userRepository.existsById(userEmail)) {
            throw new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다.");
        }
        String newPassword = RandomCertNumber.getCertNum(10);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail); //받는 사람
        message.setSubject("안녕하세요. Credit Market입니다."); //제목
        message.setText("임시 비밀번호는 [" + newPassword + "]입니다."); //내용
        message.setFrom("wpdud2003@gmail.com"); //보내는 사람
        log.info("certNum={}", newPassword);

        EntityUser userInfo = userRepository.findByUserEmail(userEmail).get();
        userInfo.setUserPassword(encoder.encode(newPassword));
        userRepository.save(userInfo);

        mailSender.send(message);

        return newPassword;
    }
}
