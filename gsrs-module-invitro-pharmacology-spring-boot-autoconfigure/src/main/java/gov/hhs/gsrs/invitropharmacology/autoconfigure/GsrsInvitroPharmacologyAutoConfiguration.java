package gov.hhs.gsrs.invitropharmacology.autoconfigure;

import gov.hhs.gsrs.invitropharmacology.services.SubstanceApiService;

import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import gsrs.api.substances.SubstanceRestApi;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

@EnableGsrsJpaEntities
@EnableGsrsApi
@AutoConfiguration
@Import({
        SubstanceApiService.class
})

public class GsrsInvitroPharmacologyAutoConfiguration {

    @Bean
    @Primary
    public JsonMapper objectMapper() {
        return JsonMapper.builderWithJackson2Defaults()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    @Bean
    public SubstanceRestApi substanceRestApi( SubstancesApiConfiguration substancesApiConfiguration){
        return new SubstanceRestApi(substancesApiConfiguration.createNewRestTemplateBuilder(), substancesApiConfiguration.getBaseURL(),
                JsonMapper.builderWithJackson2Defaults()
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .build());
    }

}