package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyReviewListResponseDto {

    private String shopImg;
    private Long reviewId;
    private String shopName;
    private Perm perm;
    private Dyeing dyeing;
    private Straightening straightening;
    private Hair_Cut hairCut;
    private Long price;
}
