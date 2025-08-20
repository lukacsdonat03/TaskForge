package hu.taskforge.TaskForge.user.controller;

import hu.taskforge.TaskForge.user.dto.UserLoginRequest;
import hu.taskforge.TaskForge.user.dto.UserLoginResponse;
import hu.taskforge.TaskForge.user.dto.UserRegisterRequest;
import hu.taskforge.TaskForge.user.dto.UserResponse;
import hu.taskforge.TaskForge.user.model.Role;
import hu.taskforge.TaskForge.user.model.User;
import hu.taskforge.TaskForge.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    /**
     *  Returns a user if it's present or return not found status
     *  it's only accessible for role ROLE_SUPERADMIN
     * @param id identity of the user
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id){
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.map(user -> {
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getEmail(),
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
            );
            return ResponseEntity.ok(userResponse);
        }).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> findAllUser(){
        List<User> users = this.userService.findAll();

        List<UserResponse> responses = users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                ))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = this.userService.login(request);
        return ResponseEntity.ok(new UserLoginResponse(token));
    }
}
