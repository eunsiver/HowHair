package review.hairshop.Auth.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//프론트가 그냥 줄 예정
public class OauthToken { //(1)
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private String id_token;
    private int refresh_token_expires_in;

}