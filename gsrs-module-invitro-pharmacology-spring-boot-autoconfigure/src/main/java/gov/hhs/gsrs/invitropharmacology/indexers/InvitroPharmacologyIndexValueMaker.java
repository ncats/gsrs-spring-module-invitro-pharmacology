package gov.hhs.gsrs.invitropharmacology.indexers;

import gov.hhs.gsrs.invitropharmacology.models.*;

import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
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
    //private SubstanceApiService substanceApiService;

    // @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    // public EntityManager entityManager;

    //  @Value("${substance.product.ivm.substancekey.resolver.touse}")
    // private String substanceKeyResolverToUseFromConfig;

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
                                if (screening.invitroReference != null) {
                                    String sourceType = screening.invitroReference.referenceSourceType != null ? screening.invitroReference.referenceSourceType : "";
                                    String source = screening.invitroReference.referenceSource != null ? screening.invitroReference.referenceSource : "";

                                    if (!sourceType.isEmpty() || !source.isEmpty()) {
                                        consumer.accept(IndexableValue.simpleFacetStringValue("Reference Type and Source", sourceType + " " + source).suggestable().setSortable());
                                    }
                                }
                            }
                        } // for loop
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
