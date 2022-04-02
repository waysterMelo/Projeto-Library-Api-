package com.waysterdemelo.LibraryApi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

//uma classe simples que representa os dados de um json
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String isbn;

    @NotEmpty
    private String author;

}
