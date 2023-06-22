package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ResponseDTO<List<Friend>>> getFriends(
            @RequestParam(defaultValue = "0") int userId) {
        if (userId <= 0) {
            return ok(friendMapper.getFriends());
        } else {
            return ok(friendMapper.getFriendsByUserId(userId));
        }
    }

    @PostMapping("/")
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
    public ResponseEntity<ResponseDTO<Friend>> deleteFriend(@PathVariable int id) {
        try {
            friendService.removeFriend(id);
            return noContent();
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }
}
