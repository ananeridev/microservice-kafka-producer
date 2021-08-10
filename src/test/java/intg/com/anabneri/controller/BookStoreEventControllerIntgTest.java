package com.anabneri.controller;

import com.anabneri.domain.Book;
import com.anabneri.domain.BookStoreEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"bookstore-events"}, partitions = 3)

// sobrescrevo propriedades do kafka brokers e dos servers
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class BookStoreEventControllerIntgTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<Integer, String> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void should_post_book_store_event() throws InterruptedException {

        //given
        Book book = Book.builder()
                .bookId(111)
                .bookName("A culpa e das estrelas")
                .bookAuthor("John Green")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(null)
                .book(book)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<BookStoreEvent> request = new HttpEntity<>(bookStoreEvent);

        //when
      ResponseEntity<BookStoreEvent> responseEntity =  restTemplate.exchange("/v1/bookstore-event", HttpMethod.POST, request, BookStoreEvent.class);

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // mostrar que sem os servidores os testes integrados nao rodam

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "bookstore-events");
        Thread.sleep(3000);
        String expectedRecord = "{\"bookStoreEventId\":null,\"bookStoreEventType\":\"NEW\",\"book\":{\"bookId\":111,\"bookName\":\"A culpa e das estrelas\",\"bookAuthor\":\"John Green\"}}";
        String value = consumerRecord.value();
        assertEquals(expectedRecord, value);
    }




    @Test
    @Timeout(6)
    void should_put_book_store_event() throws InterruptedException {

        //given
        Book book = Book.builder()
                .bookId(426)
                .bookName("A culpa e das estrelas")
                .bookAuthor("John Green")
                .build();

        BookStoreEvent bookStoreEvent = BookStoreEvent.builder()
                .bookStoreEventId(222)
                .book(book)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<BookStoreEvent> request = new HttpEntity<>(bookStoreEvent);

        //when
        ResponseEntity<BookStoreEvent> responseEntity =  restTemplate.exchange("/v1/bookstore-event", HttpMethod.PUT, request, BookStoreEvent.class);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // mostrar que sem os servidores os testes integrados nao rodam

        ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "bookstore-events");
        //Thread.sleep(3000);
        String expectedRecord = "{\"bookStoreEventId\":222,\"bookStoreEventType\":\"UPDATE\",\"book\":{\"bookId\":426,\"bookName\":\"A culpa e das estrelas\",\"bookAuthor\":\"John Green\"}}";
        String value = consumerRecord.value();
        assertEquals(expectedRecord, value);
    }
}
