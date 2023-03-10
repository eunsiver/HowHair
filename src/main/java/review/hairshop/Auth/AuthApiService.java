package review.hairshop.Auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import review.hairshop.Auth.dto.KakaoThreeInfo;
import review.hairshop.Auth.dto.KakaoUserInfoDto;
import review.hairshop.Auth.dto.PostAuthMetaReqDto;
import review.hairshop.common.enums.Screen;
import review.hairshop.common.enums.Status;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthApiService {
    private final MemberRepository memberRepository;

    public Screen postAuth(String accessToken) {
        accessToken=accessToken.substring("Bearer ".length());
        //카카오 서버에서 kakaoId, nickname, email을 가져옴
        KakaoThreeInfo kakaoThreeInfo=getKakaoUserInfo(accessToken);
        //DB 상에 존재하는지 확인
        Optional<Member> memberByKakaoId = memberRepository.findByKakaoId(kakaoThreeInfo.getKakaoId());
        if(memberByKakaoId.isEmpty()){
            // member 저장
            Member member = new Member(kakaoThreeInfo.getKakaoId(), kakaoThreeInfo.getNickname(), kakaoThreeInfo.getEmail(), null,null,null,Status.ONGOING);
            memberRepository.save(member);
            return Screen.AUTH_SCREEN;

        } else {
            // 이미 DB상에 존재하는 member Validation 처리
            if (memberByKakaoId.get().getStatus().equals(Status.ONGOING)) {
                return Screen.AUTH_SCREEN;
            } else if (memberByKakaoId.get().getStatus().equals(Status.ACTIVE)) {
                return Screen.LOGIN_SCREEN;
            } else {
                throw new ApiException(ApiResponseStatus.BLACK_MEMBER,"DB상에 INACTIVE Member 존재");
            }
        }
    }


    //!! 고민인 부분: 이 부분을 인터셉터에서 처리하는 것이 맞을지??
    //서비스 단에서 처리해도 되는지??
    private KakaoThreeInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt=new RestTemplate();

        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );


        ObjectMapper objectMapper = new ObjectMapper();

        KakaoUserInfoDto kakaoUserInfo = null;
        KakaoThreeInfo kakaoThreeInfo = null;
        try {
            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfoDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoThreeInfo.builder()
                .kakaoId(kakaoUserInfo.getId())
                .nickname(kakaoUserInfo.getKakao_account().getProfile().getNickname())
                .email(kakaoUserInfo.getKakao_account().getEmail())
                .build();
    }

    public void postAuthMeta(Long memberIdx, PostAuthMetaReqDto postAuthMetaReqDto) {
    }
}
