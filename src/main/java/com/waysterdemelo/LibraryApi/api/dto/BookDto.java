package com.waysterdemelo.LibraryApi.api.dto;

import lombok.*;

//uma classe simples que representa os dados de um json
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private long id;
    private String title;
    private String isbn;
    private String author;

}
