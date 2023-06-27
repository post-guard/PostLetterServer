package top.rrricardo.postletterserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.components.MessageWebsocketServer;
import top.rrricardo.postletterserver.mappers.FriendMapper;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.Friend;
import top.rrricardo.postletterserver.models.Message;
import top.rrricardo.postletterserver.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendService {
    private final FriendMapper friendMapper;
    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final Logger logger;

    private static final ObjectMapper s_mapper = new ObjectMapper();

    public FriendService(
            FriendMapper friendMapper,
            UserMapper userMapper,
            SessionService sessionService) {
        this.friendMapper = friendMapper;
        this.userMapper = userMapper;
        this.sessionService = sessionService;

        logger = LoggerFactory.getLogger(getClass());
    }

    @NotNull
    public Friend addFriend(int userId, int friendId) throws IllegalArgumentException {
        // 判断指定的二人是否已经是好友关系
        var friends = friendMapper.getFriends();

        Friend friend = null;
        Friend exFriend = null;
        for (var i : friends) {
            if (userId == i.getUserId() && friendId == i.getFriendId()) {
                friend = i;
            }

            if (friendId == i.getFriendId() && userId == i.getUserId()) {
                exFriend = i;
            }
        }

        var user = userMapper.getUserById(userId);
        var friendUser = userMapper.getUserById(friendId);

        if (user == null || friendUser == null) {
            throw new IllegalArgumentException("有一个用户是鬼");
        }

        if (friend == null && exFriend == null) {
            // 两者还不是好友关系
            friend = new Friend(userId, friendId);
            friendMapper.createFriend(friend);

            sendFriendMessage(friendId, user.getNickname() + "请求添加您为好友！");
        } else if (friend == null) {
            // 通过好友请求
            friend = new Friend(userId, friendId);

            var session = sessionService.createFriendSession(user, friendUser);
            friend.setSessionId(session.getId());
            exFriend.setFriendId(session.getId());

            friendMapper.createFriend(friend);
            friendMapper.updateFriend(exFriend);
        } else if (exFriend == null) {
            // 再次发送好友请求

            sendFriendMessage(friendId, user.getNickname() + "请求添加您为好友！");
        }

        return friend;
    }

    /**
     * 删除好友
     * @param friendId 需要删除的好友ID
     * @throws IllegalArgumentException 好友关系不存在
     */
    public void removeFriend(int friendId) throws IllegalArgumentException {
        var friend = friendMapper.getFriendById(friendId);

        if (friend == null) {
            throw new IllegalArgumentException("欲删除的好友关系不存在");
        }

        sessionService.destroySession(friend.getSessionId());
        friendMapper.deleteFriend(friendId);
    }

    /**
     * 查询当前邀请你为好友的人
     * @param userId 你的ID
     * @return 邀请你为好友的ID
     */
    public List<User> queryFriendRequest(int userId) {
        return null;
    }

    private void sendFriendMessage(int id, @NotNull String message) {
        // -1表示系统
        var m = new Message(-1, -1, message, LocalDateTime.now());

        try {
            var text = s_mapper.writeValueAsString(m);
            MessageWebsocketServer.sendMessage(text, id);
        } catch (JsonProcessingException e) {
            logger.error("发送好友通知失败", e);
        }
    }
}
