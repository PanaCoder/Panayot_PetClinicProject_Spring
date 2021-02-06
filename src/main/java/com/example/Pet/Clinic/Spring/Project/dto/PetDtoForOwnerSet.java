package com.example.Pet.Clinic.Spring.Project.dto;

import com.example.Pet.Clinic.Spring.Project.model.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PetDtoForOwnerSet {

    private Long id;

    private String Name;

    private PetType petType;
}
