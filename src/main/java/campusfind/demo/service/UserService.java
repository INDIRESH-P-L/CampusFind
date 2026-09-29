package campusfind.demo.service;

import campusfind.demo.entity.Role;
import campusfind.demo.entity.User;
import campusfind.demo.exception.BadRequestException;
import campusfind.demo.exception.ResourceNotFoundException;
import campusfind.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password, Role role, String phoneNumber) {
        String cleanEmail = email != null ? email.trim().toLowerCase() : "";
        if (userRepository.existsByEmailIgnoreCase(cleanEmail)) {
            throw new BadRequestException("Email is already registered: " + cleanEmail);
        }

        User user = new User();
        user.setName(name != null ? name.trim() : "");
        user.setEmail(cleanEmail);
        user.setPassword(password);
        user.setRole(role != null ? role : Role.STUDENT);
        user.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User loginUser(String email, String password) {
        String cleanEmail = email != null ? email.trim().toLowerCase() : "";
        User user = userRepository.findByEmailIgnoreCase(cleanEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + cleanEmail));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials. Incorrect password.");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
