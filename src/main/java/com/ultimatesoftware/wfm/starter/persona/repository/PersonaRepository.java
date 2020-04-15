package com.ultimatesoftware.wfm.starter.persona.repository;

import com.ultimatesoftware.wfm.starter.persona.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, UUID> {

    Optional<Persona> findById(UUID id);

}
