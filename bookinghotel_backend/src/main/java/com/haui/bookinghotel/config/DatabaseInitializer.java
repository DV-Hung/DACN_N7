package com.haui.bookinghotel.config;

import com.haui.bookinghotel.domain.User;
import com.haui.bookinghotel.repository.UserRepository;
import com.haui.bookinghotel.util.constant.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        if(this.userRepository.findByEmail("admin@gmail.com")== null){
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPassword(this.passwordEncoder.encode("123456"));
            user.setUsername("admin");
            user.setRole(Role.ADMIN);
            this.userRepository.save(user);
        }
        else {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA....");
        }
        System.out.println(">>> END INIT DATABASE");
    }
}
