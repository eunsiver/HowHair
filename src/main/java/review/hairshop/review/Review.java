package review.hairshop.review;

import lombok.*;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.CurlyStatus;
import review.hairshop.common.enums.LengthStatus;
import review.hairshop.common.enums.Satisfaction;
import review.hairshop.common.enums.Status;
import review.hairshop.hair_style_mapping.HairStyleMapping;
import review.hairshop.hair_type_mapping.HairTypeMapping;
import review.hairshop.member.Member;
import review.hairshop.review_image.ReviewImage;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Satisfaction satisfaction;


    private String hairShopName;
    private String hairShopNumber;
    private String hairShopAddress;
    private String designerName;

    @Enumerated(EnumType.STRING)
    private LengthStatus lengthStatus;

    @Enumerated(EnumType.STRING)
    private CurlyStatus curlyStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int price;

    private String content;

    private LocalDate date;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<Bookmark> bookmarkList = new LinkedList<>();

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImageList = new LinkedList<>();

    @OneToMany(mappedBy = "review")
    private List<HairTypeMapping> hairTypeMappingList = new LinkedList<>();

    @OneToMany(mappedBy = "review")
    private List<HairStyleMapping> hairStyleMappings = new LinkedList<>();

    /**
     * [변경 메서드]
     * */
    public void changeStatus(Status status){
        this.status = status;
    }

}
