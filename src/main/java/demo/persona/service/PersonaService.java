package demo.persona.service;

import demo.persona.models.Persona;
import demo.persona.repository.PersonaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonaService {

    private PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public UUID createPersona(Persona persona) {
        return personaRepository.save(persona).getId();
    }

    public Optional<Persona> getPersona(UUID id) {
        return personaRepository.findById(id);
    }

    public Page<Persona> getAllPersona(Pageable pageable) {
        return personaRepository.findAll(pageable);
    }
}
