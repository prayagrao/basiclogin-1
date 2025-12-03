package com.example.basiclogin.Service;

import com.example.basiclogin.Model.User;
import com.example.basiclogin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user) {
        // Encrypt the password before saving
        System.out.println(user);
        //String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        //user.setPassword(encryptedPassword);
        //user.setRole("USER"); // Default role is USER
        //userRepository.save(user);
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // Set role dynamically based on what was selected in the form
        // Also handle capitalization properly if needed
        String role = user.getRole().toUpperCase(); // convert to uppercase: "USER", "EDITOR", "ADMINISTRATOR"

        if (role.equals("ADMINISTRATOR")) {
            user.setRole("ADMIN");  // Save in database as "ADMIN"
        } else if (role.equals("EDITOR")) {
            user.setRole("EDITOR"); // Optional if you want to support editor role
        } else {
            user.setRole("USER"); // Default
        }

        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Replace with actual method
    }
}


