package gov.hhs.gsrs.invitropharmacology.indexers;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.services.SubstanceApiService;

import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import gsrs.substances.dto.SubstanceDTO;
import gsrs.DefaultDataSourceConfig;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.function.Consumer;
import java.util.Optional;
import java.util.List;

@Component
@Slf4j
public class InvitroPharmacologyIndexValueMaker implements IndexValueMaker<InvitroAssayInformation> {

    @Autowired
    private SubstanceApiService substanceApiService;

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    @Value("${substance.invitropharmacology.ivm.substancekey.resolver.touse}")
    private String substanceKeyResolverToUseFromConfig;

    @Override
    public Class<InvitroAssayInformation> getIndexedEntityClass() {
        return InvitroAssayInformation.class;
    }

    @Override
    public void createIndexableValues(InvitroAssayInformation invitroAssay, Consumer<IndexableValue> consumer) {
        try {
            if (invitroAssay != null) {
                if (invitroAssay.invitroAssayScreenings != null) {
                    if (invitroAssay.invitroAssayScreenings.size() > 0) {
                        for (int i = 0; i < invitroAssay.invitroAssayScreenings.size(); i++) {
                            InvitroAssayScreening screening = invitroAssay.invitroAssayScreenings.get(i);

                            if (screening != null) {
                                if (screening.invitroAssayResultInformation != null) {

                                    if (screening.invitroAssayResultInformation.invitroReferences.size() > 0) {
                                        for (InvitroReference reference : screening.invitroAssayResultInformation.invitroReferences) {
                                            if (reference != null) {
                                                if (reference.primaryReference != null) {
                                                    // only reindex the primary reference
                                                    if (reference.primaryReference == true) {
                                                        String sourceType = reference.sourceType != null ? reference.sourceType : "";
                                                        String sourceId = reference.sourceId != null ? reference.sourceId : "";

                                                        if (!sourceType.isEmpty() || !sourceId.isEmpty()) {
                                                            consumer.accept(IndexableValue.simpleFacetStringValue("Reference Source", sourceType + " " + sourceId).suggestable().setSortable());
                                                        }

                                                    } // if primaryReference is true
                                                } // if primaryRefernce is not null
                                            } // if reference object exists
                                        } // loop for each references
                                    } // if references size > 0

                                    // For Test Agent Substance Key and Substance Key Type, create IVM entity_link_substances
                                    if (screening.invitroAssayResultInformation.invitroTestAgent != null) {
                                        String substanceKey = screening.invitroAssayResultInformation.invitroTestAgent.testAgentSubstanceKey;
                                        String substanceKeyType = screening.invitroAssayResultInformation.invitroTestAgent.testAgentSubstanceKeyType;

                                        if (substanceKey != null && substanceKeyType != null) {

                                            // Get from Config which Substance Key Resolver to use. Substance API or Entity Mananger
                                            if (substanceKeyResolverToUseFromConfig != null) {
                                                if (substanceKeyResolverToUseFromConfig.equalsIgnoreCase("api")) {
                                                    // Call SUBSTANCE API Substance Resolver
                                                    createIndexableValuesBySubstanceApiResolver(consumer, substanceKey, substanceKeyType);
                                                } else {
                                                    // Call ENTITY MANAGER Substance Resolver
                                                    createIndexableValuesByEntityManagerResolver(consumer, substanceKey, substanceKeyType);
                                                }
                                            }
                                        } // substancekey and substanceKeyType exist
                                    } // if invitroTestAgent is not null

                                } // if invitroAssayResultInformation
                            } // if screening is not null
                        } // for loop screenings
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Error indexing InvitroPharmacologyIndexValueMaker:" + invitroAssay.fetchKey(), e);
        }
    }

    public void createIndexableValuesBySubstanceApiResolver(Consumer<IndexableValue> consumer, String substanceKey, String substanceKeyType) {

        // If Substance Key Type is APPROVAL_ID, BDNUM, or other key type, get the Substance record by Resolver
        if ((substanceKeyType != null) && (!substanceKeyType.equalsIgnoreCase("UUID"))) {
            // SUBSTANCE API Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, BDNUM, Other keys
            Optional<SubstanceDTO> substance = substanceApiService.getSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);

            if (substance.get() != null) {
                if (substance.get().getUuid() != null) {
                    consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substance.get().getUuid().toString()));
                }
            }
        } else {
            // If Substance Key Type is UUID, use that substanceKey
            consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substanceKey));
        }
    }

    public void createIndexableValuesByEntityManagerResolver(Consumer<IndexableValue> consumer, String substanceKey, String substanceKeyType) {

        // ENTITY MANAGER Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, BDNUM, Other keys
        Optional<Substance> substance = substanceApiService.getEntityManagerSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);

        if (substance.get() != null) {
            if (substance.get().uuid != null) {
                consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substance.get().uuid.toString()));
            }
        }
    }

}
