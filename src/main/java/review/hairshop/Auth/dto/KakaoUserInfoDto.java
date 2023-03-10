package review.hairshop.Auth.dto;

import lombok.*;

import java.util.Properties;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;
    @Data
    public class Properties { //(1)
        public String nickname;
    }
    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public class Profile {
            public String nickname;
        }
    }
}
