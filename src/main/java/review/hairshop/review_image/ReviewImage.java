package review.hairshop.review_image;


import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.Status;
import review.hairshop.review.Review;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewImage extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    private String url;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
}
