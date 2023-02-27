package review.hairshop.hair_style;

import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.Hair_Style;
import review.hairshop.hair_style_mapping.HairStyleMapping;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "hair_style")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HairStyle extends BasicEntity {

    @Id @GeneratedValue
    @Column(name = "hair_style_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Hair_Style style;

    @OneToMany(mappedBy = "hairStyle")
    private List<HairStyleMapping> hairStyleMappingList = new LinkedList<>();
}
