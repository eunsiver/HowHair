package review.hairshop.reveiwFacade.review_image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review_image.ReviewImage;
import review.hairshop.reveiwFacade.review_image.repository.ReviewImageRepository;

import java.util.ArrayList;
import java.util.List;

import static review.hairshop.common.enums.Status.ACTIVE;
import static review.hairshop.common.enums.Status.INACTIVE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public void changeImageInactive(Review review){
        List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewId(review.getId());

        if(!reviewImageList.isEmpty()){
            for(ReviewImage reviewImage:reviewImageList){
                reviewImage.changeStatus(INACTIVE);
            }
        }
    }

    @Transactional
    public void saveImage(ReviewImage img){
        reviewImageRepository.save(img);
    }

    public List<String> getReviewImages(Long reviewId) {

        List<String> imageUrls = new ArrayList<>();

        List<ReviewImage> reviewImageList = reviewImageRepository.findAllByReviewIdAndStatus(reviewId,ACTIVE);

        if(!reviewImageList.isEmpty()){
            for(ReviewImage reviewImage:reviewImageList){
                imageUrls.add(reviewImage.getUrl());
            }
        }
        return imageUrls;
    }
}
