package com.library.app.controller;

import com.library.app.dto.BookDTO;
import com.library.app.model.Book;
import com.library.app.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getBooks(){
        return ResponseEntity.ok().body(bookService.getBooks());
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId){
        return ResponseEntity.ok().body(bookService.getBookById(bookId));
    }

//    @PostMapping("/book")
//    public ResponseEntity<Book> addBook(@RequestPart Book book, @RequestPart MultipartFile bookImage) throws Exception {
//        Book book1 = bookService.addBook(book,bookImage);
//        return new ResponseEntity<>(book, HttpStatus.OK);
//    }

    @PostMapping("/book")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("/book/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId,@RequestPart Book book) {

        return new ResponseEntity<>(bookService.updateBook(bookId,book),HttpStatus.OK);
    }

    @DeleteMapping("/book/{bookId}")
    public void deleteBookById(@PathVariable Long bookId){
        bookService.deleteBookById(bookId);
    }
}
