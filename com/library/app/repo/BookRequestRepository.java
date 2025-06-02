package com.library.app.repo;

import com.library.app.model.RequestStatus;
import com.library.app.model.BookRequest;
import com.library.app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest,Long> {
    Optional<BookRequest> findByUserAndStatus(Users user, RequestStatus requestStatus);
    List<BookRequest> findByStatus(RequestStatus status);
    List<BookRequest> findByApprovalDateAfter(LocalDateTime minus);
    List<BookRequest> findByUser(Users user);
    List<BookRequest> findByStatusAndApprovalDateAfter(
            RequestStatus requestStatus,
            LocalDateTime date
    );
    List<BookRequest> findByUser_Id(Long id);
}
