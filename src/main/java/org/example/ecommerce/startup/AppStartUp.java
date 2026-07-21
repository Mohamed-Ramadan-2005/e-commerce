package org.example.ecommerce.startup;

import org.example.ecommerce.entity.Role;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.repository.RoleRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppStartUp implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@system.com");
            adminUser.setPassword(passwordEncoder.encode("123"));
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            Role adminRole = roleRepository.findByName("ADMIN").get();
            adminUser.getRoles().add(adminRole);
            userRepository.save(adminUser);
            System.out.println("Default ADMIN user created successfully.");
        }
    }
}
