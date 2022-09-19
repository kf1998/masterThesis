package cloud.user;

import cloud.user.model.User;
import cloud.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<User> users = List.of(
            User.builder()
                    .username("admin")
                    .password("password")
                    .firstname("Admin")
                    .lastname("Admin")
                    .isAdmin(true)
                    .build(),
            User.builder()
                    .username("test")
                    .password("test")
                    .firstname("Test")
                    .lastname("Test")
                    .isAdmin(false)
                    .build()
    );

    private final UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (User user : users) {
            log.info("Creating test user {}", user.getUsername());
            userService.create(user);
        }
    }
}
