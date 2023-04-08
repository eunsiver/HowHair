package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;

@Getter
@AllArgsConstructor
@Builder
public class MainReviewResponseDto {
    private String imagePath;
    private Hair_Cut hairCut;
    private Perm perm;
    private Straightening straightening;
    private Dyeing dyeing;

    private Long reviewId;

}
