package com.ultimatesoftware.wfm.starter.domain.persona;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultimatesoftware.wfm.starter.persona.controller.PersonaController;
import com.ultimatesoftware.wfm.starter.persona.models.Persona;
import com.ultimatesoftware.wfm.starter.persona.models.PersonaDto;
import com.ultimatesoftware.wfm.starter.persona.models.PersonaDtoMapper;
import com.ultimatesoftware.wfm.starter.persona.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ultimatesoftware.wfm.starter.domain.TestHelper.generatePersona;
import static com.ultimatesoftware.wfm.starter.domain.TestHelper.generatePersonaDto;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PersonaController.class)
class PersonaControllerTest {

    private final String BASE_URL = "/persona";

    @MockBean
    private PersonaService personaService;

    @MockBean
    private PersonaDtoMapper personaDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void whenCreatePersona_thenCompanySuccessfullyCreated() throws Exception {
        Persona persona = generatePersona("Anakin", "Skywalker");
        PersonaDto personaDto = generatePersonaDto(persona);

        when(personaDtoMapper.mapToDomain(personaDto)).thenReturn(persona);
        when(personaService.createPersona(persona)).thenReturn(persona.getId());

        String content = objectMapper.writeValueAsString(personaDto);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(content)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void whenQueryPersonaEndpointById_thenReturnPersona() throws Exception {
        Persona persona = generatePersona("Anakin", "Skywalker");

        when(personaService.getPersona(persona.getId())).thenReturn(Optional.of(persona));
        when(personaDtoMapper.mapToDto(persona)).thenReturn(generatePersonaDto(persona));

        mockMvc.perform(get(BASE_URL + "/" + persona.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(persona.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(persona.getLastName())))
                .andExpect(jsonPath("$.id", is(persona.getId().toString())));
    }

    @Test
    void givenPersonaDoesNotExist_whenQueryPersonaEndpointById_thenReturnNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        when(personaService.getPersona(randomUUID)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + randomUUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenCompaniesSaved_whenQueryAllCompanies_thenListOfCompaniesReturned() throws Exception {
        Persona persona1 = generatePersona("Anakin", "Skywalker");
        Persona persona2 = generatePersona("Obiwan", "Kenobi");
        List<Persona> listOfPersona = new ArrayList<>();
        listOfPersona.add(persona1);
        listOfPersona.add(persona2);

        when(personaService.getAllPersona(any(Pageable.class))).thenReturn(new PageImpl<>(listOfPersona));
        when(personaDtoMapper.mapToDto(persona1)).thenReturn(generatePersonaDto(persona1));
        when(personaDtoMapper.mapToDto(persona2)).thenReturn(generatePersonaDto(persona2));

        mockMvc.perform(get(BASE_URL + "/")
                .param("page", "1")
                .param("per_page", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(persona1.getId().toString())))
                .andExpect(jsonPath("$.content[0].firstName", is(persona1.getFirstName())))
                .andExpect(jsonPath("$.content[0].lastName", is(persona1.getLastName())));
    }
}
