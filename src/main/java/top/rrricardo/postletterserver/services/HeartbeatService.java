package top.rrricardo.postletterserver.services;

import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.components.HeartBeatWebsocketServer;
import top.rrricardo.postletterserver.exceptions.DeviceException;
import top.rrricardo.postletterserver.models.HeartBeat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Hashtable;

@Service
public class HeartbeatService {
    private final Hashtable<Integer, String> heartBeatMap = new Hashtable<>();

    /**
     * 验证用户当前使用的hostname是否正确
     * @param userId 用户ID
     * @param hostname 主机名
     * @return 若正确为真，反之为假
     */
    public boolean validateHostname(int userId, @NotNull String hostname) {
        var record = heartBeatMap.get(userId);

        if (record != null) {
            return record.equals(hostname);
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
        heartBeatMap.put(userId, hostname);
    }

    /**
     * 查询指定用户当前是否在线
     * @param userId 需要查询的用户ID
     * @return 如果在线为真，反之为假
     */
    public boolean queryOnlineState(int userId) {
        var onlineMap = HeartBeatWebsocketServer.s_heartBeatMap;

        var time = onlineMap.get(userId);

        if (time == null) {
            return false;
        }

        var duration = Duration.between(time, LocalDateTime.now());

        return duration.toSeconds() < 30;
    }
}
