package mono.shop_mono.service;

import lombok.RequiredArgsConstructor;
import mono.shop_mono.dto.exception.BadRequestException;
import mono.shop_mono.dto.exception.NotFoundException;
import mono.shop_mono.model.User;
import mono.shop_mono.model.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user) {
        List<String> uniquenessErrors = validateUniqueness(user.getUsername());
        if (!uniquenessErrors.isEmpty()) throw new BadRequestException(String.join("\n", uniquenessErrors));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    private List<String> validateUniqueness(String username) {
        List<String> errorList = new ArrayList<>();
        if (userRepository.findByUsername(username).isPresent())
            errorList.add(String.format("User %s already exists", username));
        return errorList;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found", username)));
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = findByUsername(username);
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public User update(User newUser) {
        User oldUser = findByUsername(newUser.getUsername());

        if(newUser.getPassword() != null)
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if(newUser.getFirstname() != null)
            oldUser.setFirstname(newUser.getFirstname());

        if(newUser.getLastname() != null)
            oldUser.setLastname(newUser.getLastname());

        return userRepository.save(oldUser);
    }
}