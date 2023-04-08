package review.hairshop.bookmark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.Status;
import review.hairshop.member.Member;
import review.hairshop.reveiwFacade.review.Review;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of={"id","status"})
public class Bookmark extends BasicEntity {

    @Id @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Enumerated(EnumType.STRING)
    private Status status;//ACTIVE: 좋아요, INACTIVE: 좋아요 취소

    @Builder
    public Bookmark(Member member, Review review, Status status) {

        this.member = member;
        //member.getBookmarkList().add(this);

        this.review = review;
       // review.getBookmarkList().add(this);

        this.status = status;
    }

    /**
     * [변경 메서드]
     * */
    public void changeStatus(Status status){
        this.status = status;
    }

}
