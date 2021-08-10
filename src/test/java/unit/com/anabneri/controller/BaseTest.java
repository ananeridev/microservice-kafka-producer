package com.anabneri.controller;

import com.anabneri.domain.Book;
import com.anabneri.domain.BookStoreEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseTest {

    protected Book createANewBook(){
       return Book.builder()
                .bookId(111)
                .bookName("A culpa e das estrelas")
                .bookAuthor("John Green")
                .build();
    }

    protected BookStoreEvent createANewBookWithEvent(){
        Book book =  Book.builder()
                .bookId(111)
                .bookName("A culpa e das estrelas")
                .bookAuthor("John Green")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        return bookStoreEvent;
    }
}
