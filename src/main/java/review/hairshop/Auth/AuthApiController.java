package review.hairshop.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import review.hairshop.Auth.dto.PostAuthMetaReqDto;
import review.hairshop.Auth.dto.PostAuthResDto;
import review.hairshop.common.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController {
    private final AuthApiService authApiService;
    //프론트에서 accessToken을 받고  kakaoId, kakaoEmail
    @PostMapping("")
    public ApiResponse<PostAuthResDto> postAuthpostAuth(HttpServletRequest request){

        return new ApiResponse<>(PostAuthResDto.builder()
                .screen(authApiService.postAuth(request.getHeader("Authorization")))
                .build());
    }
    /** kakaoId 로 Member 를 특정하여 name, gender, schoolName, studentId, schoolEmail, major 저장
     * Stauts -> ACTIVE
     * */

    //프론트에서 받을 값들을 PostAuthMetaReqDto로
    @PostMapping("/meta")
    public ApiResponse<String> postAuthMeta(@RequestAttribute Long memberIdx, @RequestBody PostAuthMetaReqDto postAuthMetaReqDto){

        authApiService.postAuthMeta(memberIdx,postAuthMetaReqDto);

        return new ApiResponse<>("데이터 저장에 성공했습니다.");

    }
}
