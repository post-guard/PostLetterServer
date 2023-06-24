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

    /**
     * 收到用户发送的心跳包
     * @param heartBeat 收到的心跳包
     * @throws DeviceException 心跳包中包含的hostname错误
     */
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

    /**
     * 验证用户当前使用的hostname是否正确
     * @param userId 用户ID
     * @param hostname 主机名
     * @return 若正确为真，反之为假
     */
    public boolean validateHostname(int userId, @NotNull String hostname) {
        var info = heartBeatMap.get(userId);

        if (info != null) {
            var duration = Duration.between(info.localDateTime, LocalDateTime.now());

            return duration.toSeconds() >= 60 || info.hostname.equals(hostname);
        }

        return true;
    }

    /**
     * 设置用户当前使用的设备主机名
     * 在登录时设置
     * @param userId 用户ID
     * @param hostname 主机名
     */
    public void setHostname(int userId, @NotNull String hostname) {
        var info = new HeartInfo();
        info.hostname = hostname;
        info.localDateTime = LocalDateTime.now();

        heartBeatMap.put(userId, info);
    }

    /**
     * 查询指定用户当前是否在线
     * @param userId 需要查询的用户ID
     * @return 如果在线为真，反之为假
     */
    public boolean queryOnlineState(int userId) {
        var info = heartBeatMap.get(userId);

        if (info != null) {
            var duration = Duration.between(info.localDateTime, LocalDateTime.now());

            return duration.toSeconds() < 60;
        }

        return false;
    }

    private static class HeartInfo {
        private String hostname;

        private LocalDateTime localDateTime;
    }
}
