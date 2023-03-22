package review.hairshop.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.Dyeing;
import review.hairshop.common.enums.Hair_Cut;
import review.hairshop.common.enums.Perm;
import review.hairshop.common.enums.Straightening;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewMyListInfoDto {
//    //사진 하나를 어떤걸로??
//    private String shopImg;

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
