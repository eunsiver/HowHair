package review.hairshop.Auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthTokenController {
    private final AuthTokenService authService;


    // 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달
    @GetMapping("/JwtToken")
    public ResponseEntity giveJwtToken(@RequestParam("code")String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + authService.kakaoLogin(code));
        return ResponseEntity.ok().headers(headers).body("success");

    }
    @GetMapping("/AccessToken")
    public String getAccessToken(@RequestParam("code")String code) {
        return authService.doGetAccessToken(code);
    }


//    //토큰 만료시 JWT 토큰으로 갱신 요청
//    @GetMapping("/refresh")
//    public ResponseEntity refreshToken(){
//        return ResponseEntity.ok().body("success");
//    }



}
