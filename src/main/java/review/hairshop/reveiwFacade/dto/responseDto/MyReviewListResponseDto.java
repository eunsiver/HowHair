package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.reveiwFacade.dto.ReviewMyListInfoDto;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyReviewListResponseDto {
    List<ReviewMyListInfoDto> reviewMyListResDtos;
}
