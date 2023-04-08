package review.hairshop.reveiwFacade.dto.HairSearchDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import review.hairshop.common.enums.SearchFilter;
import review.hairshop.common.enums.SurgeryPeriod;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;

import java.util.List;
@Getter
@AllArgsConstructor
public class StraighteningReviewCondition {
    private Gender gender;
    private LengthStatus lengthStatus;
    private CurlyStatus curlyStatus;
    private SurgeryPeriod surgeryPeriod;
    private List<Straightening> straighteningList;
    private Integer startPrice;
    private Integer endPrice;
    private SearchFilter searchFilter;

}
