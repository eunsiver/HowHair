package review.hairshop.hair_type_mapping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.hair_type_mapping.repository.HairTypeMappingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairTypeMappingService {
    private final HairTypeMappingRepository hairTypeMappingRepository;
}
