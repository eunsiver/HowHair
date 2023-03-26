package review.hairshop.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import review.hairshop.bookmark.service.BookmarkService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkApiController {
    private final BookmarkService bookmarkService;

    //북마크 기능
    //필터 조건에 따른 Review 리스트 조회
    //내가 쓴 리뷰 리스트 조회
    //북마크 한 리뷰 리스트 조회

    /**
     * 북마크
     * */
    @PostMapping("/{reviewId}")
    public void postBookmark(@RequestAttribute Long memberId, @PathVariable("reviewId")Long reviewId){
        bookmarkService.doBookmark(memberId,reviewId);
    }

    /**
     * 북마크 취소
     * */
    @PatchMapping("reviewId")
    public void patchBookmark(@RequestAttribute Long memberId, @PathVariable("reviewId")Long reviewId){
        bookmarkService.cancelBookmark(memberId,reviewId);
    }
}
