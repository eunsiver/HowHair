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

    private Long reviewId;
    private isReaderSameWriter isReaderSameWriter;
    private String shopName;
    private List<String> imageUrls;
    private Satisfaction satisfaction;
    private String memberName;
    private Gender gender;
    private LocalDate date;
    private String designer;
    private LocalDateTime createAt;
    private Hair_Cut hairCut;
    private Perm perm;
    private Dyeing dyeing;
    private LengthStatus lengthStatus;
    private Long price;
    private String content;
    private int bookmarkCount;
    private Status bookmarkStatus;
}
