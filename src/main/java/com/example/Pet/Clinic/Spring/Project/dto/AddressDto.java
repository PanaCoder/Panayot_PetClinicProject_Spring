package com.example.Pet.Clinic.Spring.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDto {

    private Long id;

    @NotEmpty
    private String city;

    @NotEmpty
    private String address;

    private Long ownerId;

}
