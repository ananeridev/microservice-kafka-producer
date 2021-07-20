package com.anabneri.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.anabneri.domain.BookStoreEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class BookStoreEventProducer {
    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    String topic = "bookstore-events";
    @Autowired
    ObjectMapper objectMapper;

    public void sendLibraryEvent(BookStoreEvent bookStoreEvent) throws JsonProcessingException {

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.sendDefault(key,value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    // uma OUTRA alternativa de mandar uma mensagem para o topico

        public ListenableFuture<SendResult<Integer,String>> senBookStoreEvent_Approach2(BookStoreEvent bookStoreEvent) throws JsonProcessingException {

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);

        ProducerRecord<Integer,String> producerRecord = buildProducerRecord(key, value, topic);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

        // Manda um KafkaRecord com Headers usando o KafkaTemplate
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }


    // uma alternativa de mandar uma mensagem para o topico

    public SendResult<Integer, String>sendBookStoreEventSynchronous(BookStoreEvent bookStoreEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        Integer key = bookStoreEvent.getBookStoreEventId();
        String value = objectMapper.writeValueAsString(bookStoreEvent);
        SendResult<Integer,String> sendResult = null;
        try {
            sendResult = kafkaTemplate.sendDefault(key,value).get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException/InterruptedException Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Mandando uma mensagem com a exception {}", e.getMessage());
            throw e;
        }

        return sendResult;

    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Mandando uma mensagem com a exception {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }


    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Mensagem enviada com sucesso com o a key : {}  e com o valor {} , e com a partition {}", key, value, result.getRecordMetadata().partition());
    }
}
