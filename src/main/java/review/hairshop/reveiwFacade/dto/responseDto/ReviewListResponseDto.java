package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.*;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewListResponseDto {

    private String shopImg;
    private Long reviewId;
    private String shopName;
    private Perm perm;
    private Dyeing dyeing;
    private Straightening straightening;
    private Hair_Cut hairCut;
    private Long price;
    private Status bookmarkStatus;


}
