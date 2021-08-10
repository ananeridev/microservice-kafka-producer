package com.anabneri.controller;

import com.anabneri.domain.Book;
import com.anabneri.domain.BookStoreEvent;
import com.anabneri.producer.BookStoreEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookStoreEventController.class)
@AutoConfigureMockMvc
public class BookStoreEventControllerUnitTest extends BaseTest {

    // Injetar o bean
    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    BookStoreEventProducer bookStoreEventProducer;

    @Test
    void should_post_book_store_event() throws Exception {

        // given
        Book book = Book.builder()
                .bookId(111)
                .bookName("A culpa e das estrelas")
                .bookAuthor("John Green")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

     String json =  objectMapper.writeValueAsString(bookStoreEvent);

     // usando o doNothing() eu tenho erro de instanciacao
//     doNothing().when(bookStoreEventProducer).senBookStoreEvent_Approach2(isA(BookStoreEvent.class))

        when(bookStoreEventProducer.senBookStoreEvent_Approach2(isA(BookStoreEvent.class))).thenReturn(null);


        //when - chamar o endpoint
        mockMvc.perform(post("/v1/bookstore-event")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    void should_not_post_book_store_event_when_status_code_was_4xx() throws Exception {

        // given
        Book book = Book.builder()
                .bookId(null)
                .bookName("A culpa e das estrelas")
                .bookAuthor(null)
                .build();


        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        String json =  objectMapper.writeValueAsString(bookStoreEvent);

        // usando o doNothing() eu tenho erro de instanciacao
//     doNothing().when(bookStoreEventProducer).senBookStoreEvent_Approach2(isA(BookStoreEvent.class))

        //when - chamar o endpoint
        when(bookStoreEventProducer.senBookStoreEvent_Approach2(isA(BookStoreEvent.class))).thenReturn(null);

        // expected
        String expectedErrorMessage = "book.bookAuthor - must not be blank , book.bookId - must not be null";
        mockMvc.perform(post("/v1/bookstore-event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
        .andExpect(content().string(expectedErrorMessage));
    }
}
