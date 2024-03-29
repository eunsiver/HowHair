package review.hairshop.reveiwFacade.dto.requestDto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import review.hairshop.common.enums.*;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class


ReviewReqestDto {
    //미용실 이름
    @NotNull(message = "미용실 이름은 필수 값입니다.")
    private String shopName;
    //시술 만족도
    @NotNull(message = "시술 만족도는 필수 값입니다.")
    private Satisfaction satisfaction;
    //시술 날짜
    @NotNull(message = "시술 날짜는 필수 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    //디자이너
    @NotNull(message = "디자이너 이름은 필수 값입니다.")
    private String designerName;

    //헤어 스타일
    @NotNull(message = "헤어컷은 필수 값입니다.")
    private Hair_Cut hairCut;
    @NotNull(message = "펌은 필수 값입니다.")
    private Perm perm;
    @NotNull(message = "매직은 필수 값입니다.")
    private Straightening straightening;
    @NotNull(message = "염색은 필수 값입니다.")
    private Dyeing dyeing;

    //머리 기장
    @NotNull(message = "머리 기장은 필수 값입니다.")
    private LengthStatus lengthStatus;
    //시술 가격
    @NotNull(message = "가격은 필수 값입니다.")
    private Long price;

    //사진 리스트로 넘어옴, 이미지를 안넣을 수도 있으니 ArrayList로 초기화해줌.
    @Size(max=5, message = "사진은 최대 5장 까지 등록 가능합니다.")
    private List<MultipartFile> imageFiles = new ArrayList<>();

    // 내용
    @NotNull(message = "내용은 필수 값입니다.")
    private String content;
}
