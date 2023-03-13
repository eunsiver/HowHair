package review.hairshop.member.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverLocalResponseDto {
    private String title;
    private String link;
    private String address;
    private int mapx;
    private int mapy;
}
