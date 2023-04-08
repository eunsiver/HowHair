package review.hairshop.reveiwFacade.dto.HairSearchDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import review.hairshop.common.enums.SearchFilter;
import review.hairshop.common.enums.SurgeryPeriod;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewSearchCondition {private Gender gender;
    private LengthStatus lengthStatus;
    private CurlyStatus curlyStatus;
    private SurgeryPeriod surgeryPeriod;
    private List<Hair_Cut> hairCutList=new LinkedList<>();
    private List<Perm> permList=new LinkedList<>();
    private List<Straightening> straighteningList=new LinkedList<>();
    private List<Dyeing> dyeingList;
    private Integer startPrice;
    private Integer endPrice;
    private SearchFilter searchFilter;

}
