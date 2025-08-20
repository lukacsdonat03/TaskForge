package hu.taskforge.TaskForge.user.service;

import hu.taskforge.TaskForge.user.dto.UserLoginRequest;
import hu.taskforge.TaskForge.user.dto.UserRegisterRequest;
import hu.taskforge.TaskForge.user.model.Role;
import hu.taskforge.TaskForge.user.model.User;
import hu.taskforge.TaskForge.user.repository.RoleRepository;
import hu.taskforge.TaskForge.user.repository.UserRepository;
import hu.taskforge.TaskForge.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

    public Optional<User> findById(Long id){
        return this.userRepository.findById(id);
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public String login (UserLoginRequest request){
        User user = this.userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("There is no user with the username given"));
        if(!this.passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return this.jwtTokenProvider.createToken(user.getUsername(),user.getRoles());
    }
}
