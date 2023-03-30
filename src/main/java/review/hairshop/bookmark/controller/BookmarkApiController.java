package review.hairshop.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import review.hairshop.bookmark.responseDto.BookmarkResponseMessageDto;
import review.hairshop.bookmark.responseDto.MyBookMarksResponseDto;
import review.hairshop.bookmark.service.BookmarkService;
import review.hairshop.common.response.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {
    private final BookmarkService bookmarkService;

    /**
     * 북마크
     * */
    @PutMapping("/bookmark/{reviewId}")
    public ApiResponse<BookmarkResponseMessageDto> putBookmark(@RequestAttribute Long memberId, @PathVariable Long reviewId){
        
        bookmarkService.doOnOffBookmark(memberId,reviewId);
        return ApiResponse.success(BookmarkResponseMessageDto.builder().resultMessage("북마크가 등록/삭제 되었습니다.").build());
    }
    @GetMapping("/my/bookmark/list")
    public ApiResponse<List<MyBookMarksResponseDto>> getBookmarkList(@RequestAttribute Long memberId){

        return ApiResponse.success(bookmarkService.getMyBookmarkList(memberId));
    }

}
