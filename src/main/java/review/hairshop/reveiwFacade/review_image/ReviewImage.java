package review.hairshop.reveiwFacade.review_image;


import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review.Review;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class ReviewImage extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    private String path;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Builder
    public ReviewImage(String path, Status status, Review review) {
        this.path = path;
        this.status = status;
        this.review = review;
        review.getReviewImageList().add(this);
    }


    public void changeStatus(Status status){
        this.status = status;
    }
}
