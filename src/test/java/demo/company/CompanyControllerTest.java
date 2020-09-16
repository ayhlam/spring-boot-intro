package demo.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.TestHelper;
import demo.company.controller.CompanyController;
import demo.company.models.Company;
import demo.company.models.CompanyDto;
import demo.company.models.CompanyDtoMapper;
import demo.company.service.CompanyService;
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
@WebMvcTest(controllers = CompanyController.class)
class CompanyControllerTest {

    private final String BASE_URL = "/company";

    @MockBean
    private CompanyService companyService;

    @MockBean
    private CompanyDtoMapper companyDtoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void whenCreateCompany_thenCompanySuccessfullyCreated() throws Exception {
        Company company = TestHelper.generateCompany("Galactic Empire");
        CompanyDto companyDto = TestHelper.generateCompanyDto(company);

        when(companyDtoMapper.mapToDomain(companyDto)).thenReturn(company);
        when(companyService.createCompany(company)).thenReturn(company.getId());

        String content = objectMapper.writeValueAsString(companyDto);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(content)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void whenQueryCompanyEndpointById_thenReturnCompany() throws Exception {
        Company company = TestHelper.generateCompany("Galactic Empire");

        when(companyService.getCompany(company.getId())).thenReturn(Optional.of(company));
        when(companyDtoMapper.mapToDto(company)).thenReturn(TestHelper.generateCompanyDto(company));

        mockMvc.perform(get(BASE_URL + "/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(company.getName())))
                .andExpect(jsonPath("$.id", is(company.getId().toString())));
    }

    @Test
    void givenCompanyDoesNotExist_whenQueryCompanyEndpointById_thenReturnNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        when(companyService.getCompany(randomUUID)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + randomUUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenCompaniesSaved_whenQueryAllCompanies_thenListOfCompaniesReturned() throws Exception {
        Company company1 = TestHelper.generateCompany("Galactic Empire");
        Company company2 = TestHelper.generateCompany("Rebellion");
        List<Company> listOfCompanies = new ArrayList<>();
        listOfCompanies.add(company1);
        listOfCompanies.add(company2);

        when(companyService.getAllCompanies(any(Pageable.class))).thenReturn(new PageImpl<>(listOfCompanies));
        when(companyDtoMapper.mapToDto(company1)).thenReturn(TestHelper.generateCompanyDto(company1));
        when(companyDtoMapper.mapToDto(company2)).thenReturn(TestHelper.generateCompanyDto(company2));

        mockMvc.perform(get(BASE_URL + "/")
                .param("page", "1")
                .param("per_page", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(company1.getId().toString())))
                .andExpect(jsonPath("$.content[0].name", is(company1.getName())));
    }
}
