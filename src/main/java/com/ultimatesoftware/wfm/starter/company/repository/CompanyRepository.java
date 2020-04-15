package com.ultimatesoftware.wfm.starter.company.repository;

import com.ultimatesoftware.wfm.starter.company.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findById(UUID id);

}
