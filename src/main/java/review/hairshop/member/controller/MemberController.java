package review.hairshop.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import review.hairshop.member.dto.MemberResDto;
import review.hairshop.member.service.MemberService;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
//    POST	/mypage	회원정보 저장
//    GET	/mypage/{member_id}	회원정보 조회
//    PATCH	/mypage/{member_id}	회원정보 수정
//    POST	/login	로그인

    private final MemberService memberService;


    ///프론트는 다른 api를 요청할 때 발급받은 토큰과 함께 요청
    //회원 정보 저장
    @PostMapping("/")
    public void saveUserInfo(){
        //해당 토큰 유효한지 검사.
        //유저 상태 검사
    }
    //토큰으로 자기 확인
    //DB저장되어 있는지 확인

    @GetMapping("/")
    public MemberResDto memberInfo(@RequestAttribute Long memberIdx){
        return memberService.getUserInfo(memberIdx);
    }
}
