package review.hairshop.member.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
    private Long id;
    private String username;
}
