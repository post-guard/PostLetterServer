package top.rrricardo.postletterserver.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.dtos.UserDTO;
import top.rrricardo.postletterserver.services.HeartbeatService;
import top.rrricardo.postletterserver.services.JwtService;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final HeartbeatService heartbeatService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ThreadLocal<UserDTO> local = new ThreadLocal<>();

    public AuthorizeInterceptor(JwtService jwtService, HeartbeatService heartbeatService) {
        this.jwtService = jwtService;
        this.heartbeatService = heartbeatService;
    }

    @NotNull
    public static UserDTO getOperator() {
        return local.get();
    }

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        var method = handlerMethod.getMethod();
        var authorize = method.getAnnotation(Authorize.class);

        if (authorize == null) {
            // 不需要身份验证
            return true;
        }

        var tokenHeader = request.getHeader(JwtService.Header);
        if (tokenHeader == null || !tokenHeader.startsWith(JwtService.TokenPrefix)) {
            var responseDto = new ResponseDTO<UserDTO>("No token provided");
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));

            return false;
        }

        try {
            var claims = jwtService.parseJwtToken(tokenHeader);
            var userDto = new UserDTO(
                    claims.get("userId", Integer.class),
                    claims.getIssuer(),
                    claims.get("username", String.class)
            );

            local.set(userDto);

            var hostname = claims.get("hostname", String.class);
            if (hostname == null || !heartbeatService.validateHostname(userDto.getId(), hostname)) {
                var responseDto = new ResponseDTO<UserDTO>("Hostname invalid");
                response.setStatus(401);
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return false;
            }

            return true;
        } catch (JwtException exception) {
            var responseDto = new ResponseDTO<UserDTO>(exception.getMessage());

            response.setStatus(403);
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            return false;
        }
    }
}
