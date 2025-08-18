package hu.taskforge.TaskForge.user.controller;

import hu.taskforge.TaskForge.user.dto.UserRegisterRequest;
import hu.taskforge.TaskForge.user.dto.UserResponse;
import hu.taskforge.TaskForge.user.model.Role;
import hu.taskforge.TaskForge.user.model.User;
import hu.taskforge.TaskForge.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request){
        User user = this.userService.register(request);
        UserResponse userResponse = new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getEmail(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
