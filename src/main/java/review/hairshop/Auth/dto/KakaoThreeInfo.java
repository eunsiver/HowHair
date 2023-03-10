package review.hairshop.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class KakaoThreeInfo {
    private Long kakaoId;
    private String nickname;
    private String email;
    @Builder
    public KakaoThreeInfo(Long kakaoId, String nickname, String email) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
    }
}
