package review.hairshop.bookmark.dto.responseDto;

import lombok.*;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyBookMarksResponseDto {

    private String shopImg;
    private Long reviewId;
    private String shopName;
    private Perm perm;
    private Dyeing dyeing;
    private Straightening straightening;
    private Hair_Cut hairCut;
    private Long price;
}
