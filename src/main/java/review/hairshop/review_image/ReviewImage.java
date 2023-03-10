package review.hairshop.review_image;


import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.review.Review;

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

    private String url;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
}
