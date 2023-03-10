package review.hairshop.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.common.jwt.JwtUtilts;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.dto.MemberResDto;
import review.hairshop.member.repository.MemberRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final JwtUtilts jwtUtils;
    private final MemberRepository memberRepository;

    //해당 회원 상태가 탈퇴인지 아닌지 확인
//    private void checkActive(Long memberIdx){
//        if(memberRepository.existsByIdxAndStatus(memberIdx, Status.INACTIVE)){
//            throw new ApiException(ApiResponseStatus.INACTIVE_MEMBER, "해당 회원은 회원탈퇴 한 회원입니다.");
//        }
//    }
//    //로그인 회원 인증 완료 확인
//    private void checkOnGoing(Long memberIdx){
//        if(memberRepository.existsByIdxAndStatus(memberIdx, Status.ONGOING)){
//            throw new ApiException(ApiResponseStatus.FAIL_LOGIN, "로그인 시점 : 해당 사용자는 아직 인증이 다 끝나지 않은 사용자 입니다.");
//        }
//    }
    public MemberResDto getUserInfo(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.FAIL_DECRYPT,"없음");
                });
        return new MemberResDto(member.getKakaoEmail(), member.getGender()
                , member.getLengthStatus(), member.getCurlyStatus());

    }
}
