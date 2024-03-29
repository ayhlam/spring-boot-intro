package demo;

import demo.company.models.Company;
import demo.company.models.CompanyDto;
import demo.persona.models.Persona;
import demo.persona.models.PersonaDto;

import java.util.UUID;

public class TestHelper {

    public static Company generateCompany(String name) {
        Company company = new Company();
        company.setId(UUID.randomUUID());
        company.setName(name);
        return company;
    }

    public static Persona generatePersona(String firstName, String lastName) {
        Persona persona = new Persona();
        persona.setId(UUID.randomUUID());
        persona.setFirstName(firstName);
        persona.setLastName(lastName);
        return persona;
    }

    public static CompanyDto generateCompanyDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        return companyDto;
    }

    public static PersonaDto generatePersonaDto(Persona persona) {
        PersonaDto personaDto = new PersonaDto();
        personaDto.setId(persona.getId());
        personaDto.setFirstName(persona.getFirstName());
        personaDto.setLastName(persona.getLastName());
        personaDto.setCompany(persona.getCompany());
        return personaDto;
    }
}
