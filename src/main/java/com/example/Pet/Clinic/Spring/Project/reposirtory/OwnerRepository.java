package com.example.Pet.Clinic.Spring.Project.reposirtory;

import com.example.Pet.Clinic.Spring.Project.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query(value = "SELECT o FROM Owner o WHERE o.phone = ?1")
    Optional<Owner> findByPhone(String phone);

    void deleteByPhone(String phone);
}
