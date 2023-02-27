package review.hairshop.hair_style_mapping;


import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.hair_style.HairStyle;
import review.hairshop.hair_type.HairType;
import review.hairshop.review.Review;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "hair_style_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HairStyleMapping extends BasicEntity {

    @Id @GeneratedValue
    @Column(name = "hair_style_mapping_id")
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private HairStyle hairStyle;
}
