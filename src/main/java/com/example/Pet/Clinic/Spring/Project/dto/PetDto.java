package com.example.Pet.Clinic.Spring.Project.dto;

import com.example.Pet.Clinic.Spring.Project.model.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PetDto {

    private Long id;

    @NotEmpty
    private String name;

    private PetType petType;

    private Long ownerId;

    private Set<VisitDto> visit;

}
