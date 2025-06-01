package com.library.app.controller;

import com.library.app.dto.BookRequestDTO;
import com.library.app.model.RequestStatus;
import com.library.app.service.AdminRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRequestController {
    private final AdminRequestService adminRequestService;

    @GetMapping("/all-requests")
    public ResponseEntity<List<BookRequestDTO>> getAllRequests(){
        return ResponseEntity.ok(adminRequestService.getAllRequests());
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<BookRequestDTO>> getRequestByStatus(@RequestParam RequestStatus status){
        return ResponseEntity.ok(adminRequestService.getByStatus(status));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<BookRequestDTO> getRequestById(@PathVariable Long id){
        return ResponseEntity.ok(adminRequestService.getById(id));
    }
}
