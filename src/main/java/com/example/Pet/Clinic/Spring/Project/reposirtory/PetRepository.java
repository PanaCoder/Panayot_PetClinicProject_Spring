package com.example.Pet.Clinic.Spring.Project.reposirtory;

import com.example.Pet.Clinic.Spring.Project.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
