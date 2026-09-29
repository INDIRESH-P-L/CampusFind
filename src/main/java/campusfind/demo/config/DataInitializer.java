package campusfind.demo.config;

import campusfind.demo.entity.Category;
import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.FoundStatus;
import campusfind.demo.entity.LostReport;
import campusfind.demo.entity.LostStatus;
import campusfind.demo.entity.Role;
import campusfind.demo.entity.User;
import campusfind.demo.repository.CategoryRepository;
import campusfind.demo.repository.FoundItemRepository;
import campusfind.demo.repository.LostReportRepository;
import campusfind.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    public DataInitializer(UserRepository userRepository, 
                           CategoryRepository categoryRepository,
                           LostReportRepository lostReportRepository,
                           FoundItemRepository foundItemRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    @Override
    public void run(String... args) {
        seedInitialData();
    }

    public synchronized void seedInitialData() {
        // Pre-seed Categories if none exist
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("ID Cards & Badges", "Campus student/staff IDs, driver licenses, library cards"));
            categoryRepository.save(new Category("Electronics & Chargers", "Laptops, phones, power banks, phone chargers, earbuds"));
            categoryRepository.save(new Category("Calculators", "Scientific and graphing calculators (Casio, TI, etc.)"));
            categoryRepository.save(new Category("Water Bottles & Flasks", "Hydroflasks, steel bottles, tumblers"));
            categoryRepository.save(new Category("Books & Notebooks", "Textbooks, notebooks, binders"));
            categoryRepository.save(new Category("Keys & Wallets", "Room keys, vehicle keys, purses, wallets"));
        }

        // Pre-seed Demo Users idempotently
        if (userRepository.findByEmailIgnoreCase("admin@campus.edu").isEmpty()) {
            userRepository.save(new User(null, "Campus Admin", "admin@campus.edu", "admin123", Role.ADMIN, "555-0100"));
        }
        if (userRepository.findByEmailIgnoreCase("staff@campus.edu").isEmpty()) {
            userRepository.save(new User(null, "Security Officer Dave", "staff@campus.edu", "staff123", Role.STAFF, "555-0101"));
        }
        if (userRepository.findByEmailIgnoreCase("student@campus.edu").isEmpty()) {
            userRepository.save(new User(null, "Alice Smith", "student@campus.edu", "student123", Role.STUDENT, "555-0102"));
        }
        if (userRepository.findByEmailIgnoreCase("bob@campus.edu").isEmpty()) {
            userRepository.save(new User(null, "Bob Martinez", "bob@campus.edu", "bob123", Role.STUDENT, "555-0103"));
        }
        if (userRepository.findByEmailIgnoreCase("carol@campus.edu").isEmpty()) {
            userRepository.save(new User(null, "Carol Johnson", "carol@campus.edu", "carol123", Role.STUDENT, "555-0104"));
        }

        List<Category> allCats = categoryRepository.findAll();
        if (allCats.isEmpty()) return;

        Category idCat = allCats.stream().filter(c -> c.getName().contains("ID Cards")).findFirst().orElse(allCats.get(0));
        Category electCat = allCats.stream().filter(c -> c.getName().contains("Electronics")).findFirst().orElse(allCats.get(0));
        Category calcCat = allCats.stream().filter(c -> c.getName().contains("Calculators")).findFirst().orElse(allCats.get(0));
        Category flaskCat = allCats.stream().filter(c -> c.getName().contains("Water Bottles")).findFirst().orElse(allCats.get(0));
        Category keyCat = allCats.stream().filter(c -> c.getName().contains("Keys")).findFirst().orElse(allCats.get(0));

        User staff = userRepository.findByEmailIgnoreCase("staff@campus.edu").orElse(null);
        User studentAlice = userRepository.findByEmailIgnoreCase("student@campus.edu").orElse(null);
        User studentBob = userRepository.findByEmailIgnoreCase("bob@campus.edu").orElse(null);

        // Pre-seed Found Items if none exist
        if (foundItemRepository.count() == 0 && staff != null) {
            FoundItem f1 = new FoundItem("Casio fx-991EX Classwiz Scientific Calculator",
                    "Black casing with carbon-fiber pattern lid. Found under chair row 3.",
                    "Math Department Lecture Hall B", LocalDate.now().minusDays(1), staff, calcCat);
            f1.setStatus(FoundStatus.AVAILABLE);
            foundItemRepository.save(f1);

            FoundItem f2 = new FoundItem("Apple AirPods Pro 2 with MagSafe Case",
                    "White charging case with a small green carabiner attached.",
                    "Campus Fitness Center Locker #42", LocalDate.now().minusDays(2), staff, electCat);
            f2.setStatus(FoundStatus.AVAILABLE);
            foundItemRepository.save(f2);

            FoundItem f3 = new FoundItem("Matte Black Hydro Flask (32oz)",
                    "Dented bottom corner with NASA and GitHub stickers.",
                    "Central Library Floor 2 Quiet Study", LocalDate.now().minusDays(3), staff, flaskCat);
            f3.setStatus(FoundStatus.CLAIMED);
            f3.setClaimedBy(studentAlice);
            foundItemRepository.save(f3);

            FoundItem f4 = new FoundItem("Student ID Badge & Dorm Keys",
                    "Red university lanyard with magnetic dorm tag #214.",
                    "Dining Commons Cafeteria", LocalDate.now().minusDays(5), staff, keyCat);
            f4.setStatus(FoundStatus.RETURNED);
            f4.setClaimedBy(studentBob);
            foundItemRepository.save(f4);

            FoundItem f5 = new FoundItem("Anker 65W GaN USB-C Laptop Charger",
                    "Compact black brick with 2-meter braided cable.",
                    "Engineering Hall Room 104", LocalDate.now(), staff, electCat);
            f5.setStatus(FoundStatus.AVAILABLE);
            foundItemRepository.save(f5);

            FoundItem f6 = new FoundItem("Student Campus ID Card (Alice Smith)",
                    "Blue RFID university card found by library turnstiles.",
                    "Central Library Entrance", LocalDate.now(), staff, idCat);
            f6.setStatus(FoundStatus.AVAILABLE);
            foundItemRepository.save(f6);
        }

        // Pre-seed Lost Reports if none exist
        if (lostReportRepository.count() == 0 && studentAlice != null) {
            LostReport l1 = new LostReport("Casio fx-991EX Calculator",
                    "Left my calculator right after Calculus III lecture yesterday.",
                    "Math Department Hall B", LocalDate.now().minusDays(1), studentAlice, calcCat);
            l1.setStatus(LostStatus.OPEN);
            lostReportRepository.save(l1);

            LostReport l2 = new LostReport("AirPods Pro in White Case",
                    "Dropped near the gym bench press or locker area.",
                    "Campus Fitness Center", LocalDate.now().minusDays(2), studentBob != null ? studentBob : studentAlice, electCat);
            l2.setStatus(LostStatus.OPEN);
            lostReportRepository.save(l2);

            LostReport l3 = new LostReport("Black Insulated Water Bottle",
                    "Hydroflask with space stickers, very sentimental to me.",
                    "Central Library Floor 2", LocalDate.now().minusDays(3), studentAlice, flaskCat);
            l3.setStatus(LostStatus.RESOLVED);
            lostReportRepository.save(l3);

            LostReport l4 = new LostReport("Brown Leather Trifold Wallet",
                    "Contains driver license, transit card, and dorm key.",
                    "Student Quad Courtyard", LocalDate.now().minusDays(1), studentBob != null ? studentBob : studentAlice, keyCat);
            l4.setStatus(LostStatus.OPEN);
            lostReportRepository.save(l4);
        }
    }

    public synchronized void resetDemoData() {
        foundItemRepository.deleteAll();
        lostReportRepository.deleteAll();
        seedInitialData();
    }
}
