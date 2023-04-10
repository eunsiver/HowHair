package review.hairshop.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.common.response.ApiResponseStatus;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static review.hairshop.common.consts.SessionConst.MEMBER_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        /** 1. 세션 값으로 (null이 아닌) member Id 값이 존재하면 -> 그 member Id 값을 controller로 넘기면서 , 해당 요청을 controller로 넘기고 */
        /** 2. 세션 값으로 member Id 값이 존재하지 않으면 -> 즉 null 이면 -> 해당 요청을 controller까지 넘기지 않고, 실패 응답을 보낸다 */
        HttpSession session = request.getSession(false);

        if(session==null || session.getAttribute(MEMBER_ID)==null){
            return sendNotLoginResponse(response, ApiResponseStatus.NO_LOGIN);
        }


        request.setAttribute("memberId", session.getAttribute(MEMBER_ID));
        return true;
    }



    private boolean sendNotLoginResponse(HttpServletResponse response, ApiResponseStatus status) {
        //응답의 meta 정보를 setting한 후
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            //실질적인 응답값을 , JSON 형식의 String으로 변환화여 보낸다. (단 BaseResponse라는 공통 응답 형식을 지키면서)
            String result = objectMapper.writeValueAsString(ApiResponse.fail(status));
            response.getWriter().print(result);
        }
        catch (IOException e){
            log.error("로그인되지 않은 or 로그인 유효 시간이 만료된 세션 key 값이 들어있는 요청이 들어왔습니다.");
        }

        log.error("EXCEPTION = {}, message = {}", status, status.getMessage());
        return false;
    }
}