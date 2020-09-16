package demo.company;

import demo.StarterApplication;
import demo.TestHelper;
import demo.company.models.Company;
import demo.company.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {TestHelper.class, StarterApplication.class})
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void givenCompanySaved_whenQueried_companyReturned() {
        Company company = companyRepository.save(TestHelper.generateCompany("Galactic Empire"));

        Company savedCompany = companyRepository.findById(company.getId())
                .orElseThrow(AssertionError::new);

        assertEquals(company.getName(), savedCompany.getName());
    }
}
