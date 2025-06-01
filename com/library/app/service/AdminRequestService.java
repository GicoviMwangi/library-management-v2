package com.library.app.service;

import com.library.app.dto.BookDTO;
import com.library.app.dto.BookRequestDTO;
import com.library.app.dto.UserDTO;
import com.library.app.model.BookRequest;
import com.library.app.repo.BookRequestRepository;
import com.library.app.model.RequestStatus;
import com.library.app.model.Book;
import com.library.app.repo.BooksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BooksRepository booksRepository;

    public List<BookRequest> getRecentRequest(int days) {
        return bookRequestRepository.findByApprovalDateAfter(LocalDateTime.now().minusDays(days));
    }

    @Transactional
    public void cancelRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("REQUEST NOT FOUND"));

        if (request.getStatus() == RequestStatus.APPROVED) {
            request.getBookSet().forEach(book -> {
                Book dbBook = booksRepository.findById(book.getBookId()).get();

                dbBook.setQuantity(dbBook.getQuantity() + 1);
                booksRepository.save(dbBook);
            });
        }
        request.setStatus(RequestStatus.REJECTED);
        bookRequestRepository.save(request);
    }

    public List<BookRequestDTO> getAllRequests() {
        List<BookRequest> bookRequest = bookRequestRepository.findAll();
        return bookRequest.stream().map(this::convertToDTO).toList();
    }

    public List<BookRequestDTO> getByStatus(RequestStatus status) {
        List<BookRequest> bookRequest = bookRequestRepository.findByStatus(status);

        return bookRequest.stream().map(this::convertToDTO).toList();
    }

    BookRequestDTO convertToDTO(BookRequest bookRequest){
        Set<BookDTO> brDTO = bookRequest.getBookSet().stream().map(book -> new BookDTO(book.getBookId(),book.getBookName(),book.getCategory(),book.getLevel())).collect(Collectors.toSet());
        return new BookRequestDTO(
                bookRequest.getId(),
                bookRequest.getUser().getUserId(),
                brDTO,
                bookRequest.getStatus()
        );
    }

    public BookRequestDTO getById(Long id) {
        BookRequest bookRequest = bookRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("INVALID REQUEST ID "+id+"."));
        return convertToDTO(bookRequest);
    }
}
