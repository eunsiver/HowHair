package review.hairshop.hair_type.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.hair_type.repository.HairTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairTypeService {

    private final HairTypeRepository hairTypeRepository;
}
