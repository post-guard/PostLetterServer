package top.rrricardo.postletterserver.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletterserver.models.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getUsers();

    @Nullable
    User getUserById(int id);

    void createUser(@NotNull User user);

    void updateUser(@NotNull User user);

    void deleteUser(int id);
}
