package review.hairshop.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewMyListResDto {
    List<ReviewMyListInfoDto> reviewMyListResDtos;
}
