package review.hairshop.member;

import lombok.*;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.CurlyStatus;
import review.hairshop.common.enums.Gender;
import review.hairshop.common.enums.LengthStatus;
import review.hairshop.common.enums.Status;
import review.hairshop.review.Review;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BasicEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "kakao_id")
    private Long kakaoId;
    private String kakaoNickname;
    private String kakaoEmail;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private CurlyStatus curlyStatus;

    @Enumerated(EnumType.STRING)
    private LengthStatus lengthStatus;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new LinkedList<>();

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarkList = new LinkedList<>();

    @Builder
    public Member( Long kakaoId, String kakaoNickname, String kakaoEmail, Gender gender, CurlyStatus curlyStatus, LengthStatus lengthStatus,Status status) {
        this.kakaoId = kakaoId;
        this.kakaoNickname = kakaoNickname;
        this.kakaoEmail = kakaoEmail;
        this.gender = gender;
        this.curlyStatus = curlyStatus;
        this.lengthStatus = lengthStatus;
        this.status=status;
    }
}
