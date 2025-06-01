package com.library.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long bookId;
    private String bookName;
    private String category;
    private int level;
}
