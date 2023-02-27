package review.hairshop.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonPropertyOrder({"isSuccess", "code", "message", "result", "invalidInput"})  /** Json 으로 나갈 순서를 설정하는 어노테이션*/
@JsonInclude(JsonInclude.Include.NON_NULL)                      /** Json으로 응답이 나갈 때 - null인 필드는(CLASS LEVEL에 붙었으니) 응답으로 포함시키지 않는 어노테이션 */
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;

    @JsonProperty("code")
    private final int code;

    private final String message;

    private T result;

    private T invalidInput;

    /** [팩토리 패턴 - 정적 팩토리 메서드 사용]
     * : 생성자를 private으로 정의하므로 써 , 생성자를 통한 객체 생성을 막고 , 대신 static 생성메서드를 통한 객체 생성을 유도함*/

    private ApiResponse(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    private ApiResponse(boolean isSuccess, int code, String message, T result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @JsonPropertyOrder({"isSuccess", "code", "message", "invalidInput"})
   private ApiResponse(ApiResponseStatus status, T invalidInput){
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.invalidInput = invalidInput;
   }

   /** 1. [API 성공시 나가는 응답] */
   public static ApiResponse success(){
       return new ApiResponse(ApiResponseStatus.SUCCESS.isSuccess(), ApiResponseStatus.SUCCESS.getCode(), ApiResponseStatus.SUCCESS.getMessage());
   }

   public static <T> ApiResponse<T> success(T result){
       return new ApiResponse<>(ApiResponseStatus.SUCCESS.isSuccess(), ApiResponseStatus.SUCCESS.getCode(), ApiResponseStatus.SUCCESS.getMessage(), result);
   }

   /** 2. [API 실패시 나가는 응답] */

   public static ApiResponse fail(ApiResponseStatus status){
       return new ApiResponse<>(status.isSuccess(), status.getCode(), status.getMessage());
   }

    /** 3. [API 실패시 나가는 응답 - 어떤 유효하지 않은 입력값이 들어와서 실패했는지 - 그 유효하지 않은 입력값을 함꼐 보내줌] */
    public static <T> ApiResponse<T> failWithInvalidInput(ApiResponseStatus status, T invalidInput){
        return new ApiResponse<>(status, invalidInput);
    }

    /** 4. [API 실패시 나가는 응답 - Bean Validation에 의한 검증 오류 시, 그 결과를 넣을 수 있도록 함] */
    public static <T> ApiResponse<T> failBeanValidation(T bindResult){
        return new ApiResponse<>(ApiResponseStatus.VALIDATION_FAIL, bindResult);
    }
}
