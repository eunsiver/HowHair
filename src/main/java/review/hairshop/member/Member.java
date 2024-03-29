package review.hairshop.member;

import lombok.*;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review.Review;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BasicEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private Long kakaoId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private CurlyStatus curlyStatus;

    @Enumerated(EnumType.STRING)
    private LengthStatus lengthStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new LinkedList<>();

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarkList = new LinkedList<>();

    /**
     * [변경 메서드]
     * */

    public void changeMemberInfo(Gender gender, LengthStatus lengthStatus, CurlyStatus curlyStatus){

        this.gender = gender;
        this.lengthStatus = lengthStatus;
        this.curlyStatus = curlyStatus;
    }

    public void changeMemberInfo(LengthStatus lengthStatus, CurlyStatus curlyStatus){

        this.lengthStatus = lengthStatus;
        this.curlyStatus = curlyStatus;
    }

    public void changeStatus(Status status){
        this.status = status;
    }


}
