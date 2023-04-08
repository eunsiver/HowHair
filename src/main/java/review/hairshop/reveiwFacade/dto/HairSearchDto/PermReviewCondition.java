package review.hairshop.reveiwFacade.dto.HairSearchDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import review.hairshop.common.enums.SearchFilter;
import review.hairshop.common.enums.SurgeryPeriod;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class PermReviewCondition {
    private Gender gender;
    private LengthStatus lengthStatus;
    private CurlyStatus curlyStatus;
    private SurgeryPeriod surgeryPeriod;
    private List<Perm> permList;
    private Integer startPrice;
    private Integer endPrice;
    private SearchFilter searchFilter;

}
