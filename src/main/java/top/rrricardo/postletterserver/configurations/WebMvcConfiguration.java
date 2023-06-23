package top.rrricardo.postletterserver.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.rrricardo.postletterserver.components.AuthorizeInterceptor;
import top.rrricardo.postletterserver.services.HeartbeatService;
import top.rrricardo.postletterserver.services.JwtService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final JwtService jwtService;
    private final HeartbeatService heartbeatService;

    public WebMvcConfiguration(JwtService jwtService, HeartbeatService heartbeatService) {
        this.jwtService = jwtService;
        this.heartbeatService = heartbeatService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizeInterceptor(jwtService, heartbeatService));
    }
}
