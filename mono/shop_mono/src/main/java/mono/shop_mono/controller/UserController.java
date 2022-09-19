package mono.shop_mono.controller;

import lombok.RequiredArgsConstructor;
import mono.shop_mono.config.security.JwtTokenUtil;
import mono.shop_mono.dto.request.LoginRequest;
import mono.shop_mono.dto.request.RegisterRequest;
import mono.shop_mono.dto.request.UserRequest;
import mono.shop_mono.dto.response.OrderResponse;
import mono.shop_mono.dto.response.UserResponse;
import mono.shop_mono.model.User;
import mono.shop_mono.service.OrderService;
import mono.shop_mono.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final OrderService orderService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            User user = (User) authenticate.getPrincipal();
            UserResponse userResponse = new UserResponse(user);
            userResponse.setToken(jwtTokenUtil.generateAccessToken(user));

            return ResponseEntity.ok()
                    .body(userResponse);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                false
        );

        UserResponse userResponse = new UserResponse(userService.create(user));
        userResponse.setToken(jwtTokenUtil.generateAccessToken(user));

        return userResponse;
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @PreAuthorize("#username == authentication.name || hasAuthority('ADMIN')")
    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        userService.deleteByUsername(username);
    }

    @PreAuthorize("#username == authentication.name || hasAuthority('ADMIN')")
    @GetMapping(path = "/{username}")
    public UserResponse getUser(@PathVariable String username) {
        return new UserResponse(userService.findByUsername(username));
    }


    @PreAuthorize("#username == authentication.name || hasAuthority('ADMIN')")
    @RequestMapping(path = "/{username}", method = {POST, PUT})
    public UserResponse edit(@PathVariable String username, @ModelAttribute @Valid UserRequest request) {
        User user = new User(username, request);
        return new UserResponse(userService.update(user));
    }

    @PreAuthorize("#username == authentication.name || hasAuthority('ADMIN')")
    @GetMapping(path = "/{username}/orders")
    public List<OrderResponse> getUserOrders(@PathVariable String username) {
        return orderService.findByUsername(username).stream().map(OrderResponse::new).collect(Collectors.toList());
    }
}