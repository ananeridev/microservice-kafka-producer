package com.anabneri.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class BookStoreEventProducerUnitTest {

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    // o object mapper serializa o objeto
    @Spy
    ObjectMapper objectMapper;

    @InjectMocks
    BookStoreEventProducer bookStoreEventProducer;

    @Test
    void should_validate_book_store_event_with_approach2_on_failure() {
        //given

        //when

        //then
    }
}
