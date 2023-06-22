package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.components.AuthorizeInterceptor;
import top.rrricardo.postletterserver.dtos.GroupDTO;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.services.GroupService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController extends ControllerBase {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Authorize
    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<GroupDTO>>> getGroups() {
        var operator = AuthorizeInterceptor.getOperator();

        return ok(groupService.queryUserGroup(operator.getId()));
    }

    @Authorize
    @PostMapping("/")
    public ResponseEntity<ResponseDTO<GroupDTO>> createGroup(@RequestBody GroupDTO groupDTO) {
        var operator = AuthorizeInterceptor.getOperator();

        try {
            groupService.createGroup(operator.getId(), groupDTO);

            return created();
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }

    @Authorize
    @DeleteMapping("/")
    public ResponseEntity<ResponseDTO<GroupDTO>> deleteGroup(@RequestBody GroupDTO groupDTO) {
        var operator = AuthorizeInterceptor.getOperator();

        try {
            groupService.deleteGroup(operator.getId(), groupDTO);
            return noContent();
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }
}
