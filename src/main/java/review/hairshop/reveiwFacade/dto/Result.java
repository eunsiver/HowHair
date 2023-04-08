package review.hairshop.reveiwFacade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result<T> {
    private T data;
}
