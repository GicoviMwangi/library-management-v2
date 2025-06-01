package com.library.app.repo;

import com.library.app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryUserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String username);
}
