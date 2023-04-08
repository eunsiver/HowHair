package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MainReviewResponseDto {
    private String imagePath;
    private String hashTag;

    private Long reviewId;

}
