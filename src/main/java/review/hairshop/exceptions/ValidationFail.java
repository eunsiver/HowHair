package review.hairshop.exceptions;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationFail {
    private List<ValidationFailForField> fieldList;
    private List<ValidationFailForObject> objectList;
}
