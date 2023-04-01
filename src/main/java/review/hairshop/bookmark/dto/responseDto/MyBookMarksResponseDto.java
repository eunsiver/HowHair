package review.hairshop.bookmark.dto.responseDto;

import lombok.*;
import review.hairshop.common.enums.*;

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
