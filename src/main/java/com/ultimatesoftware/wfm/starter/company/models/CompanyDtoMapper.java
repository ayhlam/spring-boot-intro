package com.ultimatesoftware.wfm.starter.company.models;

import org.springframework.stereotype.Component;

@Component
public class CompanyDtoMapper {

    public CompanyDto mapToDto(Company company) {
        CompanyDto result = new CompanyDto();
        result.setId(company.getId());
        result.setName(company.getName());
        return result;
    }

    public Company mapToDomain(CompanyDto companyDto) {
        Company result = new Company();
        result.setName(companyDto.getName());
        return result;
    }

}
