package com.library.app.controller;

import com.library.app.service.BookRequestService;
import com.library.app.model.BookRequest;
import com.library.app.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class BookRequestController {
    private final BookRequestService bookRequestService;

    @PostMapping("/submit")
    public ResponseEntity<BookRequest> submitRequest(@AuthenticationPrincipal UserPrincipal userDetails){
        Long userId = ((UserPrincipal) userDetails).getUserId();
        return ResponseEntity.ok(bookRequestService.submitRequest(userId));
    }

    @GetMapping("/my-request")
    public ResponseEntity<List<BookRequest>> getUserRequests(@AuthenticationPrincipal UserPrincipal userPrincipal){
        Long userId = (userPrincipal).getUserId();
        return ResponseEntity.ok(bookRequestService.getUserRequest(userId));
    }

    @PostMapping("/book/{bookId}")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserPrincipal userPrincipal , @PathVariable Long bookId){
        BookRequest request = bookRequestService.addToCart(userPrincipal.getUserId(),bookId);
        return ResponseEntity.ok(request);
    }
}
