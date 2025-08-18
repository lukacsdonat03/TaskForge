package hu.taskforge.TaskForge.user.service;

import hu.taskforge.TaskForge.user.dto.UserRegisterRequest;
import hu.taskforge.TaskForge.user.model.Role;
import hu.taskforge.TaskForge.user.model.User;
import hu.taskforge.TaskForge.user.repository.RoleRepository;
import hu.taskforge.TaskForge.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserRegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        Role role = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("Role not found"));

        user.getRoles().add(role);
        return this.userRepository.save(user);
    }

}
