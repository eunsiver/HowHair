package review.hairshop.bookmark.responseDto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookmarkResponseMessageDto {
    private String resultMessage;
}
