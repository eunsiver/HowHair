package review.hairshop.reveiwFacade.review;

import lombok.*;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.*;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;
import review.hairshop.member.Member;
import review.hairshop.reveiwFacade.review_image.ReviewImage;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id", "status"})
public class Review extends BasicEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Satisfaction satisfaction;

    private String hairShopName;

    private String designerName;

    @Enumerated(EnumType.STRING)
    private LengthStatus lengthStatus;

    @Enumerated(EnumType.STRING)
    private CurlyStatus curlyStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long price;

    private String content;

    private LocalDate date;


    @Enumerated(EnumType.STRING)
    private Hair_Cut hairCut;

    @Enumerated(EnumType.STRING)
    private Dyeing dyeing;
    @Enumerated(EnumType.STRING)
    private Perm perm;
    @Enumerated(EnumType.STRING)
    private Straightening straightening;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<Bookmark> bookmarkList = new LinkedList<>();

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImageList = new LinkedList<>();

    /**
     * [변경 메서드]
     */

    public void changeStatus(Status status) {
        this.status = status;
    }

    @Builder
    public Review(CurlyStatus curlyStatus, Satisfaction satisfaction, String hairShopName, String designerName, LengthStatus lengthStatus, Status status, Long price, String content, LocalDate date, Hair_Cut hairCut, Dyeing dyeing, Perm perm, Straightening straightening, Member member) {
        this.curlyStatus = curlyStatus;
        this.satisfaction = satisfaction;
        this.hairShopName = hairShopName;
        this.designerName = designerName;
        this.lengthStatus = lengthStatus;
        this.status = status;
        this.price = price;
        this.content = content;
        this.date = date;
        this.hairCut = hairCut;
        this.perm = perm;
        this.dyeing = dyeing;
        this.straightening = straightening;
        this.member = member;
    }

}
