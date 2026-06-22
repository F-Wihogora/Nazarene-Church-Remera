package com.ncrde.church.config;

import com.ncrde.church.entity.*;
import com.ncrde.church.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
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
    private DepartmentRepository departmentRepository;

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Initializing database with Nazarene Remera seed data...");

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

        // 3. Seed Admin User
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

        // 4. Seed Pastor: Rev. Jean Kabera (Picture 1)
        if (!userRepository.existsByUsername("jean_kabera")) {
            Role pastorRole = roleRepository.findByName(UserRoleType.ROLE_PASTOR).get();
            Set<Role> roles = new HashSet<>();
            roles.add(pastorRole);

            User user = new User("jean_kabera", "jean.kabera@nazarene.org", passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setEmailVerified(true);
            userRepository.save(user);

            Member member = new Member("Jean", "Kabera", "jean.kabera@nazarene.org", "+250788000001", remeraCampus);
            member.setUser(user);
            member.setGender("MALE");
            memberRepository.save(member);
            logger.info("Seeded Pastor: Rev. Jean Kabera ('jean_kabera')");
        }

        // 5. Seed Pastor/Secretary: Alice Mutoni (Picture 2)
        if (!userRepository.existsByUsername("alice_mutoni")) {
            Role secretaryRole = roleRepository.findByName(UserRoleType.ROLE_SECRETARY).get();
            Set<Role> roles = new HashSet<>();
            roles.add(secretaryRole);

            User user = new User("alice_mutoni", "alice.mutoni@nazarene.org", passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setEmailVerified(true);
            userRepository.save(user);

            Member member = new Member("Alice", "Mutoni", "alice.mutoni@nazarene.org", "+250788000002", remeraCampus);
            member.setUser(user);
            member.setGender("FEMALE");
            memberRepository.save(member);
            logger.info("Seeded Leader: Alice Mutoni ('alice_mutoni')");
        }

        // 6. Seed Pastor/Admin: Floribert Wihogora (Picture 3)
        if (!userRepository.existsByUsername("floribert_wihogora")) {
            Role pastorRole = roleRepository.findByName(UserRoleType.ROLE_PASTOR).get();
            Role adminRole = roleRepository.findByName(UserRoleType.ROLE_ADMIN).get();
            Set<Role> roles = new HashSet<>();
            roles.add(pastorRole);
            roles.add(adminRole);

            User user = new User("floribert_wihogora", "f.wihogora@nazarene.org", passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setEmailVerified(true);
            userRepository.save(user);

            Member member = new Member("Floribert", "Wihogora", "f.wihogora@nazarene.org", "+250788000003", remeraCampus);
            member.setUser(user);
            member.setGender("MALE");
            memberRepository.save(member);
            logger.info("Seeded Pastor/Admin: Floribert Wihogora ('floribert_wihogora')");
        }

        // 7. Seed Youth Leader/Choir: Sister Mutoni (Picture 4)
        if (!userRepository.existsByUsername("sister_mutoni")) {
            Role youthRole = roleRepository.findByName(UserRoleType.ROLE_YOUTH_LEADER).get();
            Role choirRole = roleRepository.findByName(UserRoleType.ROLE_CHOIR_MEMBER).get();
            Set<Role> roles = new HashSet<>();
            roles.add(youthRole);
            roles.add(choirRole);

            User user = new User("sister_mutoni", "s.mutoni@nazarene.org", passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setEmailVerified(true);
            userRepository.save(user);

            Member member = new Member("Sister", "Mutoni", "s.mutoni@nazarene.org", "+250788000004", remeraCampus);
            member.setUser(user);
            member.setGender("FEMALE");
            memberRepository.save(member);
            logger.info("Seeded Youth Leader: Sister Mutoni ('sister_mutoni')");
        }

        // 8. Seed System Settings
        seedSetting("church_name", "Nazarene Church Remera", "Official name of the local church");
        seedSetting("church_motto", "Holiness unto the Lord", "Church motto");
        seedSetting("currency", "RWF", "Local currency code used for finance tracking");
        seedSetting("timezone", "Africa/Kigali", "Church local timezone");
        seedSetting("livestream_url", "https://www.youtube.com/embed/live_stream?channel=UCxxxxxx", "Embedded livestream address");

        // 9. Seed Feature Flags
        seedFlag("livestream", true, "Enable livestream block on the public homepage");
        seedFlag("ai_chat", true, "Enable the AI assistant context chat querying engine");
        seedFlag("qr_attendance", true, "Enable QR scanners");

        // 10. Seed Worship Songs
        if (songRepository.count() == 0) {
            songRepository.save(new Song("Ndagusingiza", "G", 78, "Lyrics of praise...", "PRAISE"));
            songRepository.save(new Song("Aracyari Umuyobozi", "C", 85, "Lyrics of worship...", "WORSHIP"));
            songRepository.save(new Song("Hari Inshuti", "F", 72, "Hymn lyrics...", "HYMN"));
            logger.info("Seeded default worship songs");
        }

        // 11. Seed Cell Groups
        if (cellGroupRepository.count() == 0) {
            Member leader = memberRepository.findByEmail("f.wihogora@nazarene.org")
                    .orElse(memberRepository.findAll().get(0));
            CellGroup group1 = new CellGroup("Remera Central Cell", "Sector Remera, Cell Rukiri II", leader, remeraCampus);
            cellGroupRepository.save(group1);
            logger.info("Seeded default cell groups");
        }

        // 12. Seed Departments
        if (departmentRepository.count() == 0) {
            Member floribert = memberRepository.findByEmail("f.wihogora@nazarene.org").orElse(null);
            Member alice = memberRepository.findByEmail("alice.mutoni@nazarene.org").orElse(null);
            Member sisterMutoni = memberRepository.findByEmail("s.mutoni@nazarene.org").orElse(null);
            Member jean = memberRepository.findByEmail("jean.kabera@nazarene.org").orElse(null);

            Department ushers = new Department("Ushers Department", "Welcoming members and guest seating services.");
            ushers.setLeader(alice);
            departmentRepository.save(ushers);

            Department media = new Department("Media & Technology", "Sound engineering, livestream, projection and social media management.");
            media.setLeader(floribert);
            departmentRepository.save(media);

            Department worship = new Department("Worship Choir", "Leading the congregation in praise and adoration.");
            worship.setLeader(sisterMutoni);
            departmentRepository.save(worship);

            Department youth = new Department("Youth Fellowship (NYI)", "Spiritual growth and engagement programs for youths.");
            youth.setLeader(jean);
            departmentRepository.save(youth);

            logger.info("Seeded default departments with leaders");
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
