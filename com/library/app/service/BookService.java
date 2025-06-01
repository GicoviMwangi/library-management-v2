package com.library.app.service;

import com.library.app.dto.BookDTO;
import com.library.app.model.Book;
import com.library.app.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BooksRepository booksRepository;

    public List<BookDTO> getBooks() {
        List<Book> books = booksRepository.findAll();
        return books.stream().map(this::convertToDTO).toList();
    }

    public BookDTO getBookById(Long bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        return convertToDTO(book);
    }


//    public Book addBook(Book book, MultipartFile bookImage) throws Exception{
//        book.setBookName(bookImage.getOriginalFilename());
//        book.setBookImageType(bookImage.getContentType());
//        book.setBookImage(bookImage.getBytes());
//
//        return booksRepository.save(book);
//    }
    public Book addBook(Book book){
        return booksRepository.save(book);
    }

//    public Book updateBook(Long bookId,Book book,MultipartFile bookImage) throws Exception{
//        book.setBookName(bookImage.getOriginalFilename());
//        book.setBookImageType(bookImage.getContentType());
//        book.setBookImage(bookImage.getBytes());
//
//        return booksRepository.save(book);
//    }

    public Book updateBook(Long bookId,Book book){
        return booksRepository.save(book);
    }

    public void deleteBookById(Long bookId) {
        booksRepository.deleteById(bookId);
    }

    public Book addBookById(Long bookId){
        Book book = booksRepository.findById(bookId).orElse(null);
        return booksRepository.save(book);
    }

    BookDTO convertToDTO(Book book){
        return new BookDTO(
                book.getBookId(),
                book.getBookName(),
                book.getCategory(),
                book.getLevel()
        );
    }
}
