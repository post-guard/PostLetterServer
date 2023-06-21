package top.rrricardo.postletterserver.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletterserver.models.Friend;

import java.util.List;

@Mapper
public interface FriendMapper {
    @NotNull
    List<Friend> getFriends();

    @Nullable
    Friend getFriendById(int id);

    void createFriend(@NotNull Friend friend);

    void updateFriend(@NotNull Friend friend);

    void deleteFriend(int id);
}
