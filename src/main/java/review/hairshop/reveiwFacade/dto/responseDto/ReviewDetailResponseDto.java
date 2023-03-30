package review.hairshop.reveiwFacade.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import review.hairshop.common.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewDetailResponseDto {
    //북마크 수
    private int bookmarkCount;
    private Long reviewId;
    //이 글을 직접 쓴 사람인지
    private isReaderSameWriter isReaderSameWriter;
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
    private Hair_Cut hairCut;

    private Perm perm;

    private Dyeing dyeing;
    //리뷰에 쓴 머리 기장
    private LengthStatus lengthStatus;
    //시술 가격
    private Long price;
    //내용
    private String content;
}
