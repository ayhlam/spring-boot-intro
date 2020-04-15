package com.ultimatesoftware.wfm.starter.persona.models;

import org.springframework.stereotype.Component;

@Component
public class PersonaDtoMapper {

    public PersonaDto mapToDto(Persona persona) {
        PersonaDto result = new PersonaDto();
        result.setId(persona.getId());
        result.setFirstName(persona.getFirstName());
        result.setLastName(persona.getLastName());
        result.setCompany(persona.getCompany());
        return result;
    }

    public Persona mapToDomain(PersonaDto personaDto) {
        Persona result = new Persona();
        result.setFirstName(personaDto.getFirstName());
        result.setLastName(personaDto.getLastName());
        result.setCompany(personaDto.getCompany());
        return result;
    }
}
