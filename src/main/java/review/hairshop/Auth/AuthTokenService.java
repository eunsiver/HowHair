package review.hairshop.Auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import org.springframework.web.client.RestTemplate;
import review.hairshop.Auth.dto.KakaoUserInfoDto;
import review.hairshop.Auth.dto.OauthToken;
import review.hairshop.common.enums.Status;
import review.hairshop.common.jwt.JwtUtilts;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthTokenService {
    private final MemberRepository memberRepository;
    private final JwtUtilts jwtUtilts;
    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;
    @Value("${kakao.client_secret}")
    private String secretKey;
    RestTemplate rt=new RestTemplate();


    public String kakaoLogin(String code) {
        //인가 코드로 엑세스 토큰 요청
        OauthToken accessToken=getAccessToken(code);
        //엑세스 토큰으로 카카오 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfo=getKakaoUserInfo(accessToken.getAccess_token());
//        //필요시 회원가입
        Member kakaoUser=registerKakaoUserIfNeeded(kakaoUserInfo);
        Status userStatus=checkUserStatus(kakaoUser);
//        //로그인 JWT 토큰 발행
        String jwtToken=jwtUtilts.createAccessToken((kakaoUser.getKakaoId().toString()));
        return jwtToken;
    }
    public String doGetAccessToken(String code) {
        //인가 코드로 엑세스 토큰 요청
        OauthToken accessToken=getAccessToken(code);
        //엑세스 토큰으로 카카오 사용자 정보 가져오기
        return accessToken.getAccess_token();
    }

    private Status checkUserStatus(Member kakaoUser) {
        return kakaoUser.getStatus();
    }

    private OauthToken getAccessToken(String code){
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri",redirect_uri );
        params.add("code", code);
        params.add("client_secret", secretKey);
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken; //(8)
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken)  {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );


        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserInfoDto kakaoUserInfo = null;
        try {

            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfoDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoUserInfo;
    }


    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        //회원없으면 DB에 저장하고 그때 UserState.ONGOING: 아직 추가 정보 입력 안함
        if (kakaoUser == null) {
            System.out.println("회원없음");
            String nickname = kakaoUserInfo.getKakao_account().getProfile().getNickname();
            String email = kakaoUserInfo.getKakao_account().getEmail();
            //이메일이 없을 수도 있으며, 이 경우 카카오 계정 정보를 통한 수집(수집 후 제공) 옵션을 쓰거나 자체 수집해야한다.
            //카카오 계정 이메일이 인증 받지 않은 이메일일 수 있다. 인증받은 이메일만 사용해야하는 서비스라면 인증 여부를 확인후 미인증 이메일 사용자에게 자체적으로 인증 절차를 진행해야한다.
            kakaoUser = new Member(kakaoId,nickname,email,null,null,null, Status.ONGOING);
            memberRepository.save(kakaoUser);
            memberRepository.findByKakaoId(kakaoId).orElseThrow(
                    ()->{
                throw new ApiException(ApiResponseStatus.INTERNAL_SERVER_ERROR,"유저가 저장되지 않았습니다.");
                    }
            );

        }
        return kakaoUser;
    }



}
