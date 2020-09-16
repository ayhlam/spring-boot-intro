package demo.company.controller;

import demo.company.models.CompanyDto;
import demo.company.models.CompanyDtoMapper;
import demo.company.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@Validated
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyDtoMapper companyDtoMapper;

    public CompanyController(CompanyService companyService, CompanyDtoMapper companyDtoMapper) {
        this.companyService = companyService;
        this.companyDtoMapper = companyDtoMapper;
    }

    @PostMapping()
    public ResponseEntity<UUID> createCompany(@RequestBody CompanyDto companyDto) {
        UUID companyId = companyService.createCompany(companyDtoMapper.mapToDomain(companyDto));
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + companyId)
                .buildAndExpand(companyId).toUri()).body(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable("id") UUID id) {
        return companyService.getCompany(id)
                .map(companyDtoMapper::mapToDto)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping
    public Page<CompanyDto> getAllCompanies(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "30") int perPage) {
        return companyService.getAllCompanies(PageRequest.of(page, perPage))
                .map(companyDtoMapper::mapToDto);
    }
}

