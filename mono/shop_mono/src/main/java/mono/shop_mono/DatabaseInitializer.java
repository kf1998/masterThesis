package mono.shop_mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mono.shop_mono.model.User;
import mono.shop_mono.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<User> users = List.of(
            new User(
                    "admin",
                    "password",
                    "Admin",
                    "Admin",
                    true
            ),
            new User(
                    "test",
                    "password",
                    "Test",
                    "Test",
                    false
            )
    );

    private final UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (User user : users) {
            log.info("Creating test user {}", user.getUsername());
            user.setPassword("password");
            userService.create(user);
        }
    }
}