package review.hairshop.reveiwFacade.dto.HairSearchDto;

import lombok.AllArgsConstructor;
import lombok.Data;
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

@Data
public class ReviewSearchCondition {private Gender gender;
    private LengthStatus lengthStatus;
    private CurlyStatus curlyStatus;
    private SurgeryPeriod surgeryPeriod;
    private List<Hair_Cut> hairCut=new LinkedList<>();
    private List<Perm> perm=new LinkedList<>();
    private List<Straightening> straightening=new LinkedList<>();
    private List<Dyeing> dyeing;
    private Integer startPrice;
    private Integer endPrice;
    private SearchFilter searchFilter;

}
