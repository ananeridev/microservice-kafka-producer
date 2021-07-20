package com.anabneri.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {

    // second step
    // class to model the book from friends

    @NotNull
    private Integer bookId;

    @NotBlank
    private  String bookName;

    @NotBlank
    private String bookAuthor;

}
