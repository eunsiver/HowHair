package review.hairshop.member.dto;


import lombok.*;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoAuthDto {

    private Long id;
    private Map<String, String> properties;
}
