package review.hairshop.member.dto.response;

import lombok.*;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDto {
    private String nickname;
    private Gender gender;
    private LengthStatus lengthStatus;
    private CurlyStatus curlyStatus;
}
