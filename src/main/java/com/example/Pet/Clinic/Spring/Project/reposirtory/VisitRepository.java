package com.example.Pet.Clinic.Spring.Project.reposirtory;

import com.example.Pet.Clinic.Spring.Project.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

}
