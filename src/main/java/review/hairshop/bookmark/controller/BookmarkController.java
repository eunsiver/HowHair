package review.hairshop.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import review.hairshop.bookmark.service.BookmarkService;

@Controller
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
}
