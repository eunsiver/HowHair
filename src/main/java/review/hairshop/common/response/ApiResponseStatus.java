package review.hairshop.common.response;

import lombok.Getter;
/**
 * [ 1000단위 ] - 오류의 범위
 *  1000 : 요청 성공
 *  2 : Request 오류
 *  3 : Reponse 오류
 *  4 : DB, Server 오류
 *
 * [ 100단위 ] - 오류 도메인
 *  0 : 공통 오류
 *  1 : member 오류
 *  2 : review 오류
 *  3 : reviewImage 오류
 *  4 : bookmark 오류
 *  5 : hairType 오류
 *  6 : hairStyle 오류
 *  7 : hairTypeMapping 오류
 *  8 : hairStyleMapping 오류
 *
 *
 *
 * [10단위] - 오류 HTTP Method
 *  0~19 : Common
 *  20~39 : GET
 *  40~59 : POST
 *  60~79 : PATCH
 *  80~99 : else
 *
 *  [1 단위] - 그외 오류의 고유 식별자
 *          - 순서대로 설정해주면 됨
 */

/**  [ApiResponse 로 나갈 값들을 - 상황에 따른 열거형 값으로 미리 선언해 놓고 - 가져다 쓰는 형태를 위해서 사용]*/
@Getter
public enum ApiResponseStatus {

    /**
     * 1000 : 요청 성공
     * */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     * */
    MISSING_REQUEST_HEADER(false, 2000, "필수 헤더 값이 넘어오지 않았습니다."),
    MISSING_REQUEST_PARAMETER(false, 2001, "필수 쿼리파라미터 값이 넘어오지 않았습니다."),
    VALIDATION_FAIL(false, 2002, "요청한 값의 검증 로직에서 오류가 발견되었습니다."),
    INVALID_ENUM(false, 2003, "정의되지 않은 enum 값이 넘어왔습니다."),
    MAX_FILE_SIZE_EXCEEDED(false, 2004, "업로드 가능한 각 이미지 최대 사이즈는 15MB로 제한됩니다."),
    MAX_REQUEST_SIZE_EXCEEDED(false, 2005, "업로드 가능한 총 이미지 최대 사이즈는 100MB로 제한됩니다."),
    INACTIVE_MEMBER(false,2006,"해당 회원은 탈퇴한 회원입니다."),
    FAIL_LOGIN(false, 2007, "로그인 시점 : 해당 사용자는 아직 인증이 다 끝나지 않은 사용자 입니다."),
    NO_MEMBER_IDX(false, 2008, "테스트 시 인증을 위한 memberIdx 값이 존재하지 않습니다."),
    INAVALID_JWT_TOKEN(false, 2009, "인증 인가를 위한 jwt token값이 유효하지 않습니다."),
    NO_JWT_TOKEN(false, 2010, "인증 인가를 위한 jwt token값이 존재하지 않습니다."),
    NO_ENCRYPTED_KAKAO_ID(false, 2011, "필수 헤더값인 encrpytedKakaoId 값이 없습니다."),
    FAIL_DECRYPT(false, 2012, "AES256 복호화 과정에서 예외가 발생하였습니다."),
    INVALID_KAKAO_ID(false, 2013, "유효하지 않은 kakaoId가 들어왔습니다."),
    BLACK_MEMBER(false,2014, "가입이 불가능한 유저입니다."),
    /**
     * 3000 : Response 오류
     * */

    /**
     * 4000 : Database, Server 오류
     * */
    INTERNAL_SERVER_ERROR(false, 4000, "예상하지 못한 예외가 발생하였습니다."),
    ;




    private final boolean isSuccess;
    private final int code;
    private final String message;


    /**
     * ApiResponseStatus에서 각 해당하는 코드를 생성자로 맵핑
     * [열겨형의 생성자 - 반드시 private]
     * <이렇게 열거형 생성자를 정의하면 - 열거형 값의 선언시 , 소괄호를 통해 생성자에 인자를 전달할 수 있음 >
     *     : 그렇게 되면 결과적으로는 열거형 타입의 (열거형도 class) 객체가 생성되고 - 그 객체의 필드가 소괄호에 인자로 전달한 값 대로 초기화되는것!
     * */

    private ApiResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
