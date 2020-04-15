package com.ultimatesoftware.wfm.starter.domain.persona;

import com.ultimatesoftware.wfm.starter.StarterApplication;
import com.ultimatesoftware.wfm.starter.domain.TestHelper;
import com.ultimatesoftware.wfm.starter.persona.models.Persona;
import com.ultimatesoftware.wfm.starter.persona.repository.PersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.ultimatesoftware.wfm.starter.domain.TestHelper.generatePersona;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {TestHelper.class, StarterApplication.class})
@DataJpaTest
public class PersonaRepositoryTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Test
    public void givenPersonaSaved_whenQueried_personaReturned() {
        Persona persona = personaRepository.save(generatePersona("Anakin", "Skywalker"));

        Persona savedPersona = personaRepository.findById(persona.getId())
                .orElseThrow(AssertionError::new);

        assertEquals(persona.getFirstName(), savedPersona.getFirstName());
        assertEquals(persona.getLastName(), savedPersona.getLastName());
    }
}
