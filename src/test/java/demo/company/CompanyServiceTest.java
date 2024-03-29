package demo.company;

import demo.TestHelper;
import demo.company.models.Company;
import demo.company.repository.CompanyRepository;
import demo.company.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    private CompanyService companyService;
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        companyService = new CompanyService(companyRepository);
    }

    @Test
    void createCompany() {
        Company company = TestHelper.generateCompany("Galactic Empire");

        when(companyRepository.save(company)).thenReturn(company);

        UUID savedCompanyId = this.companyService.createCompany(company);
        assertEquals(savedCompanyId, company.getId());
    }

    @Test
    void getPersona() {
        Company company = TestHelper.generateCompany("Galactic Empire");
        when(this.companyRepository.findById(company.getId())).thenReturn(Optional.of(company));

        Optional<Company> savedCompany = this.companyService.getCompany(company.getId());
        assertTrue(savedCompany.isPresent());
        Company retrievedCompany = savedCompany.get();
        assertEquals(retrievedCompany.getId(), company.getId());
        assertEquals(retrievedCompany.getName(), company.getName());
    }

}
