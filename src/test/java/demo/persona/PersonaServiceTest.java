package demo.persona;

import demo.persona.models.Persona;
import demo.persona.repository.PersonaRepository;
import demo.persona.service.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static demo.TestHelper.generatePersona;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonaServiceTest {

    private PersonaService personaService;
    private PersonaRepository personaRepository;

    @BeforeEach
    void setup() {
        personaRepository = mock(PersonaRepository.class);
        personaService = new PersonaService(personaRepository);
    }

    @Test
    void createPersona() {
        Persona persona = generatePersona("Anakin", "Skywalker");
        when(this.personaRepository.save(persona)).thenReturn(persona);

        UUID savedPersonaId = this.personaService.createPersona(persona);
        assertEquals(savedPersonaId, persona.getId());
    }

    @Test
    void getPersona() {
        Persona persona = generatePersona("Anakin", "Skywalker");
        when(this.personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));

        Optional<Persona> savedPersona = this.personaService.getPersona(persona.getId());
        assertTrue(savedPersona.isPresent());
        Persona retrievedPersona = savedPersona.get();
        assertEquals(retrievedPersona.getId(), persona.getId());
        assertEquals(retrievedPersona.getFirstName(), persona.getFirstName());
        assertEquals(retrievedPersona.getLastName(), persona.getLastName());
    }
}
