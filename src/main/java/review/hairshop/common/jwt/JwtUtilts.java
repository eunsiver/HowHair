package review.hairshop.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtilts {
    @Value("${kakao.client_secret}")
    private String secretKey;
    private long accessTokenValidTime = Duration.ofMinutes(30).toMillis(); // 만료시간 30분
    private long refreshTokenValidTime = Duration.ofDays(14).toMillis(); // 만료시간 2주

    public String createToken(String subject,long tokenValidTime){

        //만료 기간은 1일으로 설정
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("Spring_Sever") // 토큰 발급자
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiration) //만료 시간
                .setSubject(subject) //토큰 제목, 유저의 kakaoId string값
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8))) //이 알고리즘과 , 이 시크릿 키로 서명
                .compact();
    }
    public String getKakaoId(String token){
        return extractAllClaims(token).get("kakaoId",String.class);
    }
    public Boolean isTokenExpired(String token){
        final Date expiration= extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
    public String createAccessToken(String sub) {
        return createToken(sub, accessTokenValidTime);
    }

    public String createRefreshToken(String sub) {
        return createToken(sub, refreshTokenValidTime);
    }

    public Claims extractAllClaims(String jwtToken) throws ExpiredJwtException {
        String removedBearerToken = BearerRemove(jwtToken); //Bearer 제거

        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(jwtToken)
                .getBody();

    }

    private String BearerRemove(String token){
        return token.substring("Bearer ".length());
    }
}
