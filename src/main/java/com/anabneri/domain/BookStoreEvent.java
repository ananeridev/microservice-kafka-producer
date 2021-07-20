package com.anabneri.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookStoreEvent {

    // first step
    private Integer bookStoreEventId;
    private BookStoreEventType bookStoreEventType;

    @NotNull
    @Valid
    private Book book;

}
