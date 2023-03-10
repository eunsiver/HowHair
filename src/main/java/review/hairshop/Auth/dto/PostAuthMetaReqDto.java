package review.hairshop.Auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.CurlyStatus;
import review.hairshop.common.enums.Gender;
import review.hairshop.common.enums.LengthStatus;

@NoArgsConstructor
@Getter
public class PostAuthMetaReqDto {
    private Gender gender;
    private CurlyStatus curlyStatus;
    private LengthStatus lengthStatus;
}
