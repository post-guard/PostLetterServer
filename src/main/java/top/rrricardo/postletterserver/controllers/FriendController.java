package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.mappers.FriendMapper;
import top.rrricardo.postletterserver.models.Friend;
import top.rrricardo.postletterserver.services.FriendService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController extends ControllerBase {
    private final FriendMapper friendMapper;
    private final FriendService friendService;

    public FriendController(
            FriendMapper friendMapper,
            FriendService friendService) {
        this.friendMapper = friendMapper;
        this.friendService =friendService;

    }

    @GetMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Friend>>> getFriends(
            @RequestParam(defaultValue = "0") int userId) {
        if (userId <= 0) {
            return ok(friendMapper.getFriends());
        } else {
            return ok(friendService.queryFriend(userId));
        }
    }

    @GetMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Friend>> getFriend(@PathVariable int id) {
        var friend = friendMapper.getFriendById(id);

        if (friend == null) {
            return notFound();
        } else {
            return ok(friend);
        }
    }

    @GetMapping("/request/{userId}")
    public ResponseEntity<ResponseDTO<List<Friend>>> getFriendRequest(
            @PathVariable int userId) {
        return ok(friendService.queryFriendRequest(userId));
    }

    @PostMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<Friend>> createFriend(@RequestBody Friend friend) {
        if (friend == null) {
            return badRequest();
        }

        try {
            return ok(friendService.addFriend(friend.getUserId(), friend.getFriendId()));
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Friend>> deleteFriend(@PathVariable int id) {
        try {
            var friend = friendMapper.getFriendById(id);
            if (friend == null) {
                return notFound();
            }

            friendService.removeFriend(id);
            return ok("删除成功", friend);
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }
}
