package com.library.app.service;

import com.library.app.dto.UserDTO;
import com.library.app.model.Role;
import com.library.app.model.Users;
import com.library.app.repo.LibraryUserRepository;
import com.library.app.repo.RoleRepository;
import com.library.app.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryUserService {
    private final LibraryUserRepository libraryUserRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    public UserDTO register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> managedRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role managedRole = roleRepository.findByRoleName(role.getRoleName()).orElseGet(() -> roleRepository.save(role));
            managedRoles.add(managedRole);
        }

        user.setRoles(managedRoles);
        Users users = libraryUserRepository.save(user);
        UserDTO userDTO = convertToDto(users);

        return userDTO;
    }

    public String validate(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        throw new UsernameNotFoundException("USERNAME/PASSWORD INCORRECT");
    }

    public void addRoleToUser(String username, String roleName) {
        Users user = libraryUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        if (user == null) throw new UsernameNotFoundException("USER NOT FOUND");

        Role role = roleRepository.findByRoleName(roleName).orElseGet(() -> roleRepository.save(new Role(null, roleName)));
        user.getRoles().add(role);

    }

    public UserDTO getUsers() {
        Users user = (Users) libraryUserRepository.findAll();
        UserDTO userDTO = convertToDto(user);
        return userDTO;
    }

    public UserDTO currentUser(Long id) {
        Users users = libraryUserRepository.findById(id).get();
        UserDTO userDTO = convertToDto(users);
        return userDTO;
    }

    private UserDTO convertToDto(Users user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled()
        );
    }
}
