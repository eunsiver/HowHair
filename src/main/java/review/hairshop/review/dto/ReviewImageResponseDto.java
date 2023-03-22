package review.hairshop.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ReviewImageResponseDto {
    List<String> reviewImages;
}
