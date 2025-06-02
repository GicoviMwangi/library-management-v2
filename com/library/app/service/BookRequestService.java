package com.library.app.service;

import com.library.app.dto.BookDTO;
import com.library.app.dto.BookRequestDTO;
import com.library.app.model.RequestStatus;
import com.library.app.model.Book;
import com.library.app.dto.UserDTO;
import com.library.app.model.BookRequest;
import com.library.app.model.Users;
import com.library.app.repo.BookRequestRepository;
import com.library.app.repo.BooksRepository;
import com.library.app.repo.LibraryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final LibraryUserRepository userRepository;
    private final BookRequestRepository bookRequestRepository;
    private final BooksRepository booksRepository;
    private final LibraryUserRepository libraryUserRepository;


    public BookRequestDTO submitRequest(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found "+userId+"."));

        BookRequest request = bookRequestRepository.findByUserAndStatus(user,RequestStatus.PENDING).orElseThrow(() -> new RuntimeException("No pending request to submit for user " + userId));;

        //checks the number of books issued to the user
        if (request.getBookSet().size() >= 3) throw new RuntimeException("REACHED MAXIMUM AMOUNT OF BOOKS ISSUED.");

        for (Book bookSet:request.getBookSet()){
            //check if the books' id valid in the db
            Book dbBook = booksRepository.findById(bookSet.getBookId()).orElseThrow(() -> new RuntimeException("BOOK ID IS NOT VALID "+bookSet.getBookId()+"."));

            //check if there are available copies of the books
            if (dbBook.getQuantity() <= 0) {
                //rejects the requests
               request.setStatus(RequestStatus.REJECTED);
               bookRequestRepository.save(request);
               throw new RuntimeException("Book out of stock: " + dbBook.getBookName());
            }
        }

        String serialNumber  = generateSerialNumber();

        //decrements the quantity of books in the db and saves the new quantity
        request.getBookSet().forEach(book -> {
            Book bookDb = booksRepository.findById(book.getBookId()).orElseThrow();
            bookDb.setQuantity(bookDb.getQuantity()-1);
            booksRepository.save(bookDb);
                }
        );


        request.setStatus(RequestStatus.APPROVED);
        request.setSerialNumber(serialNumber);
        request.setApprovalDate(LocalDateTime.now());

         BookRequest approvedRequest = bookRequestRepository.save(request);
         return convertToDTO(approvedRequest);
    }
    
    //return all requests of the specified user
    public List<BookRequestDTO> getUserRequest (Long userId){
        List<BookRequest> bookRequest = bookRequestRepository.findByUser_Id(userId);
        return bookRequest.stream().map(this::convertToDTO).toList();
    }

    private String generateSerialNumber(){
        return "LIB-"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+
                "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public BookRequestDTO addToCart(Long userId, Long bookId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        Book book = booksRepository.findById(bookId).orElseThrow(()-> new RuntimeException("404, BOOK NOT FOUND"));


        BookRequest request = bookRequestRepository.findByUserAndStatus(user,RequestStatus.PENDING).orElseGet(() -> {
            BookRequest newRequest = new BookRequest();
            newRequest.setUser(user);
            newRequest.setStatus(RequestStatus.PENDING);
            newRequest.setRequestDate(LocalDateTime.now());
            return bookRequestRepository.save(newRequest);
        });

        if (request.getBookSet().size() >= 3) {
            throw new RuntimeException("Cannot add more than 3 books to cart.");
        }

        if (!request.getBookSet().contains(book)){
            request.getBookSet().add(book);
            bookRequestRepository.save(request);
            return convertToDTO(request);
        }
        return convertToDTO(request);
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

}


