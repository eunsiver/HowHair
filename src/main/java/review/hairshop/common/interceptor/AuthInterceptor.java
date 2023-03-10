package review.hairshop.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import review.hairshop.common.enums.Status;
import review.hairshop.common.jwt.JwtUtilts;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtilts jwtUtils;
    private final MemberRepository memberRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private boolean testResultValue;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(Boolean.parseBoolean(request.getHeader("isTest"))){

            Optional.ofNullable(request.getHeader("memberIdx")).ifPresentOrElse(
                    k -> sendSuccessTest(request, Long.parseLong(k)),
                    () -> {sendFailTest(response, ApiResponseStatus.NO_MEMBER_IDX);}

            );
            return testResultValue;
        }



        /**  1. jwtUtil을 사용하여 토큰에 저장된 memberIdx 정보를 꺼냄 */
        Optional<Claims> memberIdx = Optional.ofNullable(request.getHeader("Authorization"))
                .map(t -> jwtUtils.extractAllClaims(t));


        // 만약 해석된 kakaoId 값이 존재하지 않는다면 -> 이는 필수 헤더인 token 값이 존재하지 않았기 떄문 -> 그에 따른 응답을 보낸다
        if(memberIdx.isEmpty()){
            return sendErrorResponse(response, ApiResponseStatus.NO_JWT_TOKEN);
        }

        // jwt token에 저장된 memberIdx를 꺼내서 , 그 값이 유효한지를 판단
        if(!memberRepository.existsByIdAndStatus(Long.parseLong(memberIdx.get().getSubject()), Status.ACTIVE)){
            return sendErrorResponse(response, ApiResponseStatus.INAVALID_JWT_TOKEN);
        }

        /** 2. jwt token이 존재하고 , 그 token이 유효하다면 (그 token안에 든 kakaoId가 유효) -> preHandle() 에서 true를 반환하여 ,
         * Controller에게 요청이 전달하게 해줌 -> 즉 인가를 실행 */


        request.setAttribute("memberIdx",Long.parseLong( memberIdx.get().getSubject()));
        return true;
    }

    /** 토큰이 존재하지 않거나 , 유효하지 않은 문제상황인 경우 -> 그 문제상황에 예외를 터뜨리지 않고 , direct로 JSON 응답을 보내는 경우 */
    private boolean sendErrorResponse(HttpServletResponse response, ApiResponseStatus status){



        //응답의 meta 정보를 setting한 후
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            //실질적인 응답값을 , JSON 형식의 String으로 변환화여 보낸다. (단 BaseResponse라는 공통 응답 형식을 지키면서)
            String result = objectMapper.writeValueAsString(new ApiResponse<>(status));
            response.getWriter().print(result);
        }
        catch (IOException e){
            log.error("필수 헤더인 token값이 들어오지 않았거나 , 유효하지 않은 toekn 값이 들어와, 그에따른 응답을 처리하는 과정에서 IOException이 발생하였습니다.");
        }

        log.error("EXCEPTION = {}, message = {}", status, status.getMessage());
        return false;
    }

    private Boolean sendSuccessTest(HttpServletRequest request, Long memberIdx){
        request.setAttribute("memberIdx", memberIdx);
        this.testResultValue = true;
        return true;
    }

    private Boolean sendFailTest(HttpServletResponse response, ApiResponseStatus status){

        //응답의 meta 정보를 setting한 후
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            //실질적인 응답값을 , JSON 형식의 String으로 변환화여 보낸다. (단 BaseResponse라는 공통 응답 형식을 지키면서)
            String result = objectMapper.writeValueAsString(new ApiResponse<>(status));
            response.getWriter().print(result);
        }
        catch (IOException e){
            log.error("테스트 시 필수 헤더값인 memberIdx값이 들어오지 않아, 그에따른 응답을 처리하는 과정에서 IOException이 발생하였습니다.");
        }

        log.error("EXCEPTION = {}, message = {}", status, status.getMessage());
        this.testResultValue = false;
        return false;
    }
}
