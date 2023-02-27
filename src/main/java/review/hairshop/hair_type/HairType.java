package review.hairshop.hair_type;


import lombok.*;
import review.hairshop.common.BasicEntity;
import review.hairshop.common.enums.Hair_Type;
import review.hairshop.hair_type_mapping.HairTypeMapping;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Table(name = "hair_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HairType extends BasicEntity {

    @Id @GeneratedValue
    @Column(name = "hair_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Hair_Type type;

    @OneToMany(mappedBy = "hairType")
    private List<HairTypeMapping> hairTypeMappingLong = new LinkedList<>();
}
