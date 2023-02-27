package review.hairshop.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import review.hairshop.review.service.ReviewService;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
}
