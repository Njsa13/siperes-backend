package com.siperes.siperes.seeders;

import com.siperes.siperes.enumeration.EnumRole;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.model.User;
import com.siperes.siperes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.FAILED_ADD_ADMIN;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
    }

    private void seedAdmin() {
        try {
            List<String> adminUsernames = Arrays.asList("admin1", "admin2");
            List<String> adminNames = Arrays.asList("Admin 1", "Admin 2");
            List<String> adminEmails = Arrays.asList("admin1@siperes.com", "admin2@siperes.com");
            List<String> adminPasswords = Arrays.asList("admin1Password!", "admin2Password!");

            List<User> users = IntStream.range(0, adminUsernames.size())
                    .mapToObj(i -> {
                        User user = User.builder()
                                .username(adminUsernames.get(i))
                                .name(adminNames.get(i))
                                .email(adminEmails.get(i))
                                .isVerifiedEmail(true)
                                .password(passwordEncoder.encode(adminPasswords.get(i)))
                                .role(EnumRole.ADMIN)
                                .status(EnumStatus.ACTIVE).build();

                        Boolean isUsernameExists = userRepository.existsByUsername(user.getUsername());
                        Boolean isEmailExists = userRepository.existsByEmail(user.getEmail());

                        return !(isUsernameExists || isEmailExists) ? user : null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            userRepository.saveAll(users);
        } catch (Exception e) {
            log.warn(FAILED_ADD_ADMIN);
            log.error(e.getMessage());
        }
    }
}
