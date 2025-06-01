package com.library.app.dto;

import com.library.app.model.Book;
import com.library.app.model.RequestStatus;
import com.library.app.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.coyote.Request;

import java.util.Set;

@Data
@AllArgsConstructor
@Getter
public class BookRequestDTO {
    private Long id;
    private Long userId;
    private Set<BookDTO> bookSet;
    private RequestStatus status;
}
