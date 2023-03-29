package review.hairshop.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import review.hairshop.bookmark.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {
    private final BookmarkService bookmarkService;

    /**
     * 북마크
     * */
    @PutMapping("/bookmark/{reviewId}")
    public void patchBookmark(@RequestAttribute Long memberId, @PathVariable("reviewId")Long reviewId){
        bookmarkService.doBookmarkUseRedisson(memberId,reviewId);
    }

//
//    /**
//     * 북마크 취소
//     * */
//    @PatchMapping("/bookmark/{reviewId}")
//    public void patchBookmark(@RequestAttribute Long memberId, @PathVariable("reviewId")Long reviewId){
//        bookmarkService.cancelBookmark(memberId,reviewId);
//    }
//    @GetMapping("/my/bookmark/list")
//    public ApiResponse<List<MyBookMarksResponseDto>> getBookmarkList(@RequestAttribute Long memberId){
//
//        return ApiResponse.success(bookmarkService.getMyBookmarkList(memberId));
//    }
}
