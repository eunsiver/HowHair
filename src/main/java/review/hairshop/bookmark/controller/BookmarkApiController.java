package review.hairshop.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import review.hairshop.bookmark.responseDto.BookmarkResponseMessageDto;
import review.hairshop.bookmark.service.BookmarkService;
import review.hairshop.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {
    private final BookmarkService bookmarkService;

    /**
     * 북마크
     * */
    @PutMapping("/bookmark/{reviewId}")
    public ApiResponse<BookmarkResponseMessageDto> patchBookmark(@RequestAttribute Long memberId, @PathVariable("reviewId")Long reviewId){
        
        bookmarkService.doOnOffBookmark(memberId,reviewId);
        return ApiResponse.success(BookmarkResponseMessageDto.builder().resultMessage("북마크가 등록되었습니다.").build());
    }

}
