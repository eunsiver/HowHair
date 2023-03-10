package review.hairshop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import review.hairshop.common.enums.CurlyStatus;
import review.hairshop.common.enums.Gender;
import review.hairshop.common.enums.LengthStatus;

@Data@AllArgsConstructor
public class MemberResDto {
    //카카오 이메일
    private String kakaoEmail;
    //성별
    private Gender gender;
    //머리 기장
    private LengthStatus lengthStatus;
    //곱슬 정도
    private CurlyStatus curlyStatus;
}
