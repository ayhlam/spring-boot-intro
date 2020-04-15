package com.ultimatesoftware.wfm.starter.persona.controller;

import com.ultimatesoftware.wfm.starter.persona.models.PersonaDto;
import com.ultimatesoftware.wfm.starter.persona.models.PersonaDtoMapper;
import com.ultimatesoftware.wfm.starter.persona.service.PersonaService;
import com.ultimatesoftware.wfm.starter.shared.WfmPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@Validated
@RequestMapping(value = "/persona", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaDtoMapper personaDtoMapper;

    public PersonaController(PersonaService personaService, PersonaDtoMapper personaDtoMapper) {
        this.personaService = personaService;
        this.personaDtoMapper = personaDtoMapper;
    }

    @PostMapping()
    public ResponseEntity<UUID> createPersona(@RequestBody PersonaDto personaDto) {
        UUID personaId = personaService.createPersona(personaDtoMapper.mapToDomain(personaDto));
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + personaId)
                .buildAndExpand(personaId).toUri()).body(personaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDto> getPersona(@PathVariable("id") UUID id) {
        return personaService.getPersona(id)
                .map(personaDtoMapper::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<PersonaDto> getAllPersona(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "30") int perPage) {
        return personaService.getAllPersona(new WfmPageRequest(page, perPage))
                .map(personaDtoMapper::mapToDto);
    }
}
