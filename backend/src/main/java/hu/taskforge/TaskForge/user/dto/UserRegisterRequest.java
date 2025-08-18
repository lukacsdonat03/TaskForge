package hu.taskforge.TaskForge.user.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private String username;
    private String password;
    private String email;
    private String name;

}
