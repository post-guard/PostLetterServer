package top.rrricardo.postletterserver.services;

import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.exceptions.DeviceException;
import top.rrricardo.postletterserver.models.HeartBeat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Hashtable;

@Service
public class HeartbeatService {
    private final Hashtable<Integer, HeartInfo> heartBeatMap = new Hashtable<>();
    private final JwtService jwtService;
    private final Logger logger;

    public HeartbeatService(JwtService jwtService) {
        this.jwtService = jwtService;
        logger = LoggerFactory.getLogger(getClass());
    }

    public void receiveHeartBeat(@NotNull HeartBeat heartBeat) throws DeviceException {
        try {
            var claims = jwtService.parseJwtToken(heartBeat.getToken());

            var userId = claims.get("userId", Integer.class);
            var oldInfo = heartBeatMap.get(userId);

            if (oldInfo != null) {
                var duration = Duration.between(oldInfo.localDateTime, LocalDateTime.now());

                if (duration.toSeconds() < 60 && !oldInfo.hostname.equals(heartBeat.getHostname())) {
                    throw new DeviceException(heartBeat.getHostname());
                }
            }

            var info = new HeartInfo();
            info.hostname = heartBeat.getHostname();
            info.localDateTime = LocalDateTime.now();
            heartBeatMap.put(userId, info);
        } catch (JwtException e) {
            logger.warn("解析JWT令牌失败", e);
        }
    }

    public void setHostname(int userId, @NotNull String hostname) {
        var info = new HeartInfo();
        info.hostname = hostname;
        info.localDateTime = LocalDateTime.now();

        heartBeatMap.put(userId, info);
    }

    private static class HeartInfo {
        private String hostname;

        private LocalDateTime localDateTime;
    }
}
