package cloud.user.controller;

import cloud.user.dto.request.UserCreateRequest;
import cloud.user.dto.request.UserUpdateRequest;
import cloud.user.dto.request.LoginRequest;
import cloud.user.dto.request.RegisterRequest;
import cloud.user.dto.response.UserResponse;
import cloud.user.model.User;
import cloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.findAll().stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{username}")
    public UserResponse getUser(@PathVariable String username) {
        return UserResponse.of(userService.findByUsername(username));
    }

    @PostMapping
    public UserResponse create(@RequestBody @Valid UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .isAdmin(false)
                .build();
        return UserResponse.of(userService.create(user));
    }

//    @RequestMapping(path = "/{username}", method = {POST, PUT})
//    public UserResponse update(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request) {
//        User user = User.builder()
//                .username(username)
//                .password(request.getPassword())
//                .firstname(request.getFirstName())
//                .lastname(request.getLastName())
//                .build();
//        return UserResponse.of(userService.update(user));
//    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        userService.deleteByUsername(username);
    }



    //////////////DRUT
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {

        try {
            User user = userService.findByUsername(request.getUsername());
            UserResponse userResponse =  UserResponse.of(user);
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                true
        );
        UserResponse userResponse = UserResponse.of(userService.create(user));
        return userResponse;
    }
}
