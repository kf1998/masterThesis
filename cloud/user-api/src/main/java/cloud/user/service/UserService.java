package cloud.user.service;

import cloud.user.repository.UserRepository;
import cloud.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("User %s not found", username)
                ));
    }

    @Transactional
    public User create(User user) {
        List<String> uniquenessErrors = validateUniqueness(user.getUsername());

        if (!uniquenessErrors.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.join("\n", uniquenessErrors)
            );

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User update(User newUser) {
        User oldUser = findByUsername(newUser.getUsername());

        if (newUser.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if (newUser.getFirstname() != null)
            oldUser.setFirstname(newUser.getFirstname());

        if (newUser.getLastname() != null)
            oldUser.setLastname(newUser.getLastname());

        return userRepository.save(oldUser);
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = findByUsername(username);
        userRepository.deleteById(user.getId());
    }

    private List<String> validateUniqueness(String username) {
        List<String> errorList = new ArrayList<>();
        if (userRepository.findByUsername(username).isPresent())
            errorList.add(String.format("User %s already exists", username));

        return errorList;
    }


}
