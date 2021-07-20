package com.anabneri.controller;

import com.anabneri.domain.BookStoreEvent;
import com.anabneri.domain.BookStoreEventType;
import com.anabneri.producer.BookStoreEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;


@RestController
public class BookStoreEventController {

    @Autowired BookStoreEventProducer bookStoreEventProducer;

    // third step
    // create and test te invocation of the controller
    @PostMapping("/v1/bookstore-event")
    public ResponseEntity<BookStoreEvent> postBookStoreEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException  {

        // um novo livro e adicionado na bilioca online
        bookStoreEvent.setBookStoreEventType(BookStoreEventType.NEW);
        // dispara o evento
        bookStoreEventProducer.senBookStoreEvent_Approach2(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookStoreEvent);
    }

    @PutMapping("/v1/bookstore-event")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException {


        if(bookStoreEvent.getBookStoreEventId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eai parca passa o bookStoreEventId aeeee senao nao rola");
        }

        bookStoreEvent.setBookStoreEventType(BookStoreEventType.UPDATE);
        bookStoreEventProducer.senBookStoreEvent_Approach2(bookStoreEvent);
        return ResponseEntity.status(HttpStatus.OK).body(bookStoreEvent);
    }
}
