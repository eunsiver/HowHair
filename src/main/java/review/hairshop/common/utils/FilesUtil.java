package review.hairshop.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import review.hairshop.common.s3.AwsS3Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilesUtil {
    private final AwsS3Service awsS3Service;
    @Value("${cloud.aws.url}")
    private String url;

    //샘플 이미지 생성
    public List<String> getSampleUrlList() {
        return List.of(url+"/sampleImg.png");
    }
    //리스트들로

    //이름 생성
    public boolean isCorrectFormImageList(List<MultipartFile> imageFiles){
        return imageFiles.stream()
                .map(i->FilenameUtils.getExtension(i.getOriginalFilename()))
                .allMatch(e -> e.equals("jpeg") || e.equals("jpg") || e.equals("png") || e.equals("JPG") || e.equals("JPEG") || e.equals("PNG"));
    }

    public void putImageInS3(List<String> imgPath,List<MultipartFile>multipartFiles) {
        IntStream.range(0, multipartFiles.size())
                .forEach(i-> {
                    awsS3Service.upload(imgPath.get(i), multipartFiles.get(i));
                });
    }

    public List<String> createImagePath(Long reviewId,List<MultipartFile> imageFiles) {

        //날짜
       LocalDate date=LocalDate.now();

        String today=date.getYear()+"-"+date.getMonthValue()+"-"+date.getDayOfMonth();
        //리뷰
        String reviewNum= "review"+reviewId;

        //return image
        return imageFiles.stream()
                .map(i->
                        "image/"+today+"/"+reviewNum+"/"+ UUID.randomUUID().toString().substring(0,8)+"."+FilenameUtils.getExtension(i.getOriginalFilename())
                                ).collect(Collectors.toList());
    }
    public List<String> getImageUrlList(List<String> pathList){
        return pathList.stream()
                .map(p -> url + "/" + p)
                .collect(Collectors.toList());
    }
}
