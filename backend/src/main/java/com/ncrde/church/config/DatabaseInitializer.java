package com.ncrde.church.config;

import com.ncrde.church.entity.*;
import com.ncrde.church.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private CellGroupRepository cellGroupRepository;

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing database with seed data...");

        // 1. Seed Campuses
        Campus remeraCampus = campusRepository.findByCode("REMERA")
                .orElseGet(() -> campusRepository.save(new Campus("Nazarene Remera Campus", "Kigali, Remera", "REMERA")));
        Campus kacyiruCampus = campusRepository.findByCode("KACYIRU")
                .orElseGet(() -> campusRepository.save(new Campus("Nazarene Kacyiru Campus", "Kigali, Kacyiru", "KACYIRU")));

        // 2. Seed Roles
        for (UserRoleType roleType : UserRoleType.values()) {
            if (roleRepository.findByName(roleType).isEmpty()) {
                roleRepository.save(new Role(roleType));
                logger.info("Seeded role: {}", roleType);
            }
        }

        // 3. Seed Admin User & Member
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(UserRoleType.ROLE_ADMIN).get();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User adminUser = new User("admin", "admin@ncrde.org", passwordEncoder.encode("password"));
            adminUser.setRoles(roles);
            adminUser.setEmailVerified(true);
            userRepository.save(adminUser);

            Member adminMember = new Member("System", "Administrator", "admin@ncrde.org", "+250780000001", remeraCampus);
            adminMember.setUser(adminUser);
            memberRepository.save(adminMember);
            logger.info("Seeded Admin user 'admin' with password 'password'");
        }

        // 4. Seed Pastor User & Member
        if (!userRepository.existsByUsername("pastor")) {
            Role pastorRole = roleRepository.findByName(UserRoleType.ROLE_PASTOR).get();
            Set<Role> roles = new HashSet<>();
            roles.add(pastorRole);

            User pastorUser = new User("pastor", "pastor@ncrde.org", passwordEncoder.encode("password"));
            pastorUser.setRoles(roles);
            pastorUser.setEmailVerified(true);
            userRepository.save(pastorUser);

            Member pastorMember = new Member("Jean", "Kabera", "pastor@ncrde.org", "+250780000002", remeraCampus);
            pastorMember.setUser(pastorUser);
            memberRepository.save(pastorMember);
            logger.info("Seeded Pastor user 'pastor' with password 'password'");
        }

        // 5. Seed Treasurer User & Member
        if (!userRepository.existsByUsername("treasurer")) {
            Role treasurerRole = roleRepository.findByName(UserRoleType.ROLE_TREASURER).get();
            Set<Role> roles = new HashSet<>();
            roles.add(treasurerRole);

            User treasurerUser = new User("treasurer", "treasurer@ncrde.org", passwordEncoder.encode("password"));
            treasurerUser.setRoles(roles);
            treasurerUser.setEmailVerified(true);
            userRepository.save(treasurerUser);

            Member treasurerMember = new Member("Alice", "Mutoni", "treasurer@ncrde.org", "+250780000003", remeraCampus);
            treasurerMember.setUser(treasurerUser);
            memberRepository.save(treasurerMember);
            logger.info("Seeded Treasurer user 'treasurer' with password 'password'");
        }

        // 6. Seed System Settings
        seedSetting("church_name", "Nazarene Church Remera", "Official name of the local church");
        seedSetting("church_motto", "Holiness unto the Lord", "Church motto");
        seedSetting("currency", "RWF", "Local currency code used for finance tracking");
        seedSetting("timezone", "Africa/Kigali", "Church local timezone");
        seedSetting("livestream_url", "https://www.youtube.com/embed/live_stream?channel=UCxxxxxx", "Embedded livestream address");

        // 7. Seed Feature Flags
        seedFlag("livestream", true, "Enable livestream block on the public homepage");
        seedFlag("ai_chat", true, "Enable the AI assistant context chat querying engine");
        seedFlag("qr_attendance", true, "Enable QR scanners inside the admin dashboard");

        // 8. Seed Worship Songs
        if (songRepository.count() == 0) {
            songRepository.save(new Song("Ndagusingiza", "G", 78, "Lyrics of praise...", "PRAISE"));
            songRepository.save(new Song("Aracyari Umuyobozi", "C", 85, "Lyrics of worship...", "WORSHIP"));
            songRepository.save(new Song("Hari Inshuti", "F", 72, "Hymn lyrics...", "HYMN"));
            logger.info("Seeded default worship songs");
        }

        // 9. Seed Cell Groups
        if (cellGroupRepository.count() == 0) {
            Member leader = memberRepository.findByEmail("treasurer@ncrde.org")
                    .orElse(memberRepository.findAll().get(0));
            CellGroup group1 = new CellGroup("Remera Central Cell", "Sector Remera, Cell Rukiri II", leader, remeraCampus);
            cellGroupRepository.save(group1);
            logger.info("Seeded default cell groups");
        }

        logger.info("Database seeding completed.");
    }

    private void seedSetting(String key, String value, String desc) {
        if (systemSettingRepository.findBySettingKey(key).isEmpty()) {
            systemSettingRepository.save(new SystemSetting(key, value, desc));
            logger.info("Seeded system setting: {} -> {}", key, value);
        }
    }

    private void seedFlag(String name, boolean enabled, String desc) {
        if (featureFlagRepository.findByFlagName(name).isEmpty()) {
            featureFlagRepository.save(new FeatureFlag(name, enabled, desc));
            logger.info("Seeded feature flag: {} -> {}", name, enabled);
        }
    }
}
