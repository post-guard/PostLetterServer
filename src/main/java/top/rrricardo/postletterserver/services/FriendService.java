package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.mappers.FriendMapper;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.Friend;

@Service
public class FriendService {
    private final FriendMapper friendMapper;
    private final UserMapper userMapper;
    private final SessionService sessionService;

    public FriendService(
            FriendMapper friendMapper,
            UserMapper userMapper,
            SessionService sessionService) {
        this.friendMapper = friendMapper;
        this.userMapper = userMapper;
        this.sessionService = sessionService;
    }

    @NotNull
    public Friend addFriend(int userId, int friendId) throws IllegalArgumentException {
        // 判断指定的二人是否已经是好友关系
        var friends = friendMapper.getFriends();

        Friend oldFriend = null;
        for (var friend : friends) {
            if ((friend.getUserId() == userId && friend.getFriendId() == friendId)
            || (friend.getUserId() == friendId && friend.getFriendId() == userId)) {
                oldFriend = friend;
                break;
            }
        }

        if (oldFriend != null) {
            return oldFriend;
        }

        // 二人还不是好友关系
        var user = userMapper.getUserById(userId);
        var friend = userMapper.getUserById(friendId);

        if (user == null || friend == null) {
            // 有一个是鬼
            throw new IllegalArgumentException("指定的用户不存在");
        }

        var session = sessionService.createFriendSession(user, friend);
        // 同时创建两边的好友
        // 但是使用同一个会话
        var friend1 = new Friend(userId, friendId);
        var friend2 = new Friend(friendId, userId);
        friend1.setSessionId(session.getId());
        friend2.setSessionId(session.getId());

        friendMapper.createFriend(friend1);
        friendMapper.createFriend(friend2);

        return friend1;
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
}
