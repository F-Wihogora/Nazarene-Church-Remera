package com.ncrde.church.controller;

import com.ncrde.church.config.JwtUtils;
import com.ncrde.church.config.UserDetailsImpl;
import com.ncrde.church.dto.*;
import com.ncrde.church.entity.*;
import com.ncrde.church.repository.CampusRepository;
import com.ncrde.church.repository.MemberRepository;
import com.ncrde.church.repository.RoleRepository;
import com.ncrde.church.repository.UserRepository;
import com.ncrde.church.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new LoginResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(UserRoleType.ROLE_USHER)
                    .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_USHER)));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(UserRoleType.ROLE_ADMIN)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_ADMIN)));
                        roles.add(adminRole);
                        break;
                    case "pastor":
                        Role pastorRole = roleRepository.findByName(UserRoleType.ROLE_PASTOR)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_PASTOR)));
                        roles.add(pastorRole);
                        break;
                    case "treasurer":
                        Role treasurerRole = roleRepository.findByName(UserRoleType.ROLE_TREASURER)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_TREASURER)));
                        roles.add(treasurerRole);
                        break;
                    case "secretary":
                        Role secretaryRole = roleRepository.findByName(UserRoleType.ROLE_SECRETARY)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_SECRETARY)));
                        roles.add(secretaryRole);
                        break;
                    case "media":
                    case "media_team":
                        Role mediaRole = roleRepository.findByName(UserRoleType.ROLE_MEDIA_TEAM)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_MEDIA_TEAM)));
                        roles.add(mediaRole);
                        break;
                    case "youth_leader":
                        Role youthRole = roleRepository.findByName(UserRoleType.ROLE_YOUTH_LEADER)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_YOUTH_LEADER)));
                        roles.add(youthRole);
                        break;
                    default:
                        Role defaultRole = roleRepository.findByName(UserRoleType.ROLE_USHER)
                                .orElseGet(() -> roleRepository.save(new Role(UserRoleType.ROLE_USHER)));
                        roles.add(defaultRole);
                }
            });
        }

        user.setRoles(roles);
        user.setEmailVerified(true); // Seeding as verified for mock simplicity
        userRepository.save(user);

        // Resolve or create Campus
        String campusCode = signUpRequest.getCampusCode();
        if (campusCode == null || campusCode.isBlank()) {
            campusCode = "REMERA";
        }
        final String finalCampusCode = campusCode.toUpperCase();
        Campus campus = campusRepository.findByCode(finalCampusCode)
                .orElseGet(() -> campusRepository.save(new Campus("Nazarene Remera Campus", "Kigali, Remera", finalCampusCode)));

        // Create Member linked to the User
        Member member = new Member(signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                signUpRequest.getPhone(),
                campus);
        member.setUser(user);
        memberRepository.save(member);

        return ResponseEntity.ok(new MessageResponse("User registered successfully as a church Member!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    // Rotate the refresh token
                    RefreshToken rotatedToken = refreshTokenService.createRefreshToken(user.getId());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, rotatedToken.getToken()));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
