package com.example.Pet.Clinic.Spring.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VisitDto {

    private Long id;

    private LocalDate creationDate = LocalDate.now();

    @NotEmpty
    private String treatment;

    private Long petId;

}
