package gov.hhs.gsrs.invitropharmacology;

import gov.hhs.gsrs.invitropharmacology.controllers.*;
import gov.hhs.gsrs.invitropharmacology.LegacyInvitroPharmacologySearcher;
import gov.hhs.gsrs.invitropharmacology.LegacyAssayScreeningSearcher;
import gov.hhs.gsrs.invitropharmacology.services.*;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class InvitroPharmacologySelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                InvitroPharmacologyEntityService.class.getName(),
                LegacyInvitroPharmacologySearcher.class.getName(),
                InvitroPharmacologyController.class.getName(),

                InvitroAssayScreeningEntityService.class.getName(),
                LegacyAssayScreeningSearcher.class.getName(),
                InvitroAssayScreeningController.class.getName()
        };
    }
}
