package top.rrricardo.postletterserver.mappers;

import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import top.rrricardo.postletterserver.models.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getUsers();

    @Nullable
    User getUserById(int id);

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(int id);
}
