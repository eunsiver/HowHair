package review.hairshop.common.response;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/** RuntimeException을 상속받은 예외여야 CheckedException 이 된다. - 아마도.. */
@Slf4j
@Getter @Setter
public class ApiException extends RuntimeException{

    private ApiResponseStatus status;
    private String internalMessage;

    public ApiException(ApiResponseStatus status, String internalMessage){
        this.status = status;
        this.internalMessage = internalMessage;
    }

}
