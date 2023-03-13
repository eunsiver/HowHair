package review.hairshop.member.dto.request;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotNull(message = "이름은 필수 입니다.")
    private String name;

    @NotNull(message = "아이디는 필수 입니다.")
    private String username;

    @NotNull(message = "패스워드는 필수 입니다.")
    private String password;
}
