package review.hairshop.bookmark.responseDto;

import lombok.*;
import review.hairshop.common.enums.Dyeing;
import review.hairshop.common.enums.Hair_Cut;
import review.hairshop.common.enums.Perm;
import review.hairshop.common.enums.Straightening;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyBookMarksResponseDto {
    private String shopImg;
    //리뷰 id
    private Long reviewId;
    //헤어샵
    private String shopName;
    //헤어스타일 종류들
    private Perm perm;
    private Dyeing dyeing;
    private Straightening straightening;
    private Hair_Cut hairCut;
    //가격
    private Long price;
}
