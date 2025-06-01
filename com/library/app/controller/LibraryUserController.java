package com.library.app.controller;


import com.library.app.model.Role;
import com.library.app.dto.UserDTO;
import com.library.app.model.Users;
import com.library.app.service.LibraryUserService;
import com.library.app.service.RoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LibraryUserController{
    private final LibraryUserService userService;
    private final RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody Users user){
        //do a check for username present in the db
        System.out.println("Register api");
        return ResponseEntity.ok().body(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        System.out.println("login api");
        return ResponseEntity.ok().body(userService.validate(user));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> currentUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.currentUser(id));
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return new ResponseEntity<>(userService.getUsers(),HttpStatus.OK);
    }

    public ResponseEntity<?> saveRole(Role role){
        return new ResponseEntity<>(roleService.saveRole(role), HttpStatus.OK);
    }

    public ResponseEntity addRoleToUser(RoleToUser user){
        userService.addRoleToUser(user.getUsername(),user.getRoleName());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity assignRoleToUser(String username,String roleName){
        userService.addRoleToUser(username,roleName);
        return ResponseEntity.ok().build();
    }
}
@Data
class RoleToUser{
    private String username;
    private String roleName;
}