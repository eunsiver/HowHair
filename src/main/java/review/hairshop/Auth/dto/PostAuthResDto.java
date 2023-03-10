package review.hairshop.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.Screen;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostAuthResDto {
    private Screen screen;
}
