package review.hairshop.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import review.hairshop.common.config.S3Config;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class FileUtil {
    private final S3Config s3Config;

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {

        if (fileName==null) {
            throw new ApiException(ApiResponseStatus.WRONG_IMAGE,"파일이 없습니다.");
        }

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");

        String idxFileName = FilenameUtils.getExtension(fileName);
        if (!fileValidate.contains(idxFileName)) {
            throw new ApiException(ApiResponseStatus.WRONG_IMAGE,"이미지 형식이 잘못되었습니다.");
        }
        return idxFileName;
    }
}
