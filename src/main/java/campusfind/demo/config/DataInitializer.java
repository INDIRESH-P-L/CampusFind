package campusfind.demo.config;

import campusfind.demo.entity.Category;
import campusfind.demo.entity.Role;
import campusfind.demo.entity.User;
import campusfind.demo.repository.CategoryRepository;
import campusfind.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        // Pre-seed Categories if none exist
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("ID Cards & Badges", "Campus student/staff IDs, driver licenses, library cards"));
            categoryRepository.save(new Category("Electronics & Chargers", "Laptops, phones, power banks, phone chargers, earbuds"));
            categoryRepository.save(new Category("Calculators", "Scientific and graphing calculators (Casio, TI, etc.)"));
            categoryRepository.save(new Category("Water Bottles & Flasks", "Hydroflasks, steel bottles, tumblers"));
            categoryRepository.save(new Category("Books & Notebooks", "Textbooks, notebooks, binders"));
            categoryRepository.save(new Category("Keys & Wallets", "Room keys, vehicle keys, purses, wallets"));
        }

        // Pre-seed Demo Users if none exist
        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "Campus Admin", "admin@campus.edu", "admin123", Role.ADMIN, "555-0100"));
            userRepository.save(new User(null, "Security Staff John", "staff@campus.edu", "staff123", Role.STAFF, "555-0101"));
            userRepository.save(new User(null, "Student Alice", "student@campus.edu", "student123", Role.STUDENT, "555-0102"));
        }
    }
}
