package review.hairshop.reveiwFacade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import review.hairshop.common.enums.*;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class ReviewParamDto {

    //미용실 이름
    private String shopName;
    //시술 만족도
    private Satisfaction satisfaction;
    //시술 날짜
    private LocalDate date;
    //디자이너
    private String designerName;

    //헤어 스타일
    private Hair_Cut hairCut;

    private Perm perm;

    private Straightening straightening;

    private Dyeing dyeing;

    //머리 기장
    private LengthStatus lengthStatus;
    //시술 가격
    private Long price;

    //사진 리스트로 넘어옴
    private List<MultipartFile> imageFiles;

    // 내용
    private String content;

    private CurlyStatus curlyStatus;
}
