package review.hairshop.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.Gender;
import review.hairshop.common.enums.LengthStatus;
import review.hairshop.common.enums.Reader;
import review.hairshop.common.enums.Satisfaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewAllInfoDto {
    //이 글을 직접 쓴 사람인지 확인
    private Reader reader;
    //헤어샵 이름
    private String shopName;
    //리뷰 사진들
    private List<String> imageUrls;
    //별점
    private Satisfaction satisfaction;
    //리뷰어 이름
    private String memberName;
    //리뷰어 성별
    private Gender gender;
    //시술 날짜
    private LocalDate date;
    //디자이너
    private String designer;
    //작성 날짜
    private LocalDateTime createAt;
    //시술 종류
    private List<String> hairTypes;
    //헤어 스타일
    private List<String> hairStyles;
    //리뷰에 쓴 머리 기장
    private LengthStatus lengthStatus;
    //시술 가격
    private Long price;
    //내용
    private String content;

    //북마크 수
}
