package demo.company.service;

import demo.company.models.Company;
import demo.company.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public UUID createCompany(Company company) {
        return companyRepository.save(company).getId();
    }

    public Optional<Company> getCompany(UUID id) {
        return companyRepository.findById(id);
    }

    public Page<Company> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

}
