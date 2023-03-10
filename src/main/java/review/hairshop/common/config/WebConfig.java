//package review.hairshop.common.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import review.hairshop.common.interceptor.AuthInterceptor;
////import review.hairshop.common.interceptor.BeforeAuthInterceptor;
////import review.hairshop.common.interceptor.FirstAuthInterceptor;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
////    private final FirstAuthInterceptor firstAuthInterceptor;
////    private final BeforeAuthInterceptor beforeAuthInterceptor;
//    private final AuthInterceptor authInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
////        registry.addInterceptor(firstAuthInterceptor)
////                .order(1)
////                .addPathPatterns("/auth")
////                .excludePathPatterns("/css/**", "/*.ico", "/error", "/auth/email", "/auth/meta", "/auth/login",
////                        "/room/**", "/matched/room/url/**", "/auth/quit", "/room/member");
////
////        registry.addInterceptor(beforeAuthInterceptor)
////                .order(2)
////                .addPathPatterns("/auth/email", "/auth/meta", "/auth/login")
////                .excludePathPatterns("/css/**", "/*.ico", "/error", "/room/**", "/test/**", "/matched/room/url/**", "/ocr", "/auth/quit", "/auth");
//
////        registry.addInterceptor(authInterceptor)
////                .order(3)
////                .addPathPatterns("/room/**", "/matched/room/url/**", "/auth/quit", "/matched/room/**")
////                .excludePathPatterns("/css/**", "/*.ico", "/error", "/auth", "/auth/login", "/auth/email", "/auth/meta", "/ocr", "/test/**");
//    }
//}
