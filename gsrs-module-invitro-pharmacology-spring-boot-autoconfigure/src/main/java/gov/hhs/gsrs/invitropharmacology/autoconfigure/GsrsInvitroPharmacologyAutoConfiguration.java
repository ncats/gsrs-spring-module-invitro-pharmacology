package gov.hhs.gsrs.invitropharmacology.autoconfigure;

import gov.hhs.gsrs.invitropharmacology.services.SubstanceApiService;

import gsrs.api.substances.SubstanceRestApi;
import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableGsrsJpaEntities
@EnableGsrsApi
@Configuration
@Import({
        SubstanceApiService.class
})
public class GsrsInvitroPharmacologyAutoConfiguration {
    private ObjectMapper mapper = new ObjectMapper();

    @Bean
    public SubstanceRestApi substanceRestApi(SubstancesApiConfiguration substancesApiConfiguration){
        return new SubstanceRestApi(substancesApiConfiguration.createNewRestTemplateBuilder(), substancesApiConfiguration.getBaseURL(), mapper);
    }
}
