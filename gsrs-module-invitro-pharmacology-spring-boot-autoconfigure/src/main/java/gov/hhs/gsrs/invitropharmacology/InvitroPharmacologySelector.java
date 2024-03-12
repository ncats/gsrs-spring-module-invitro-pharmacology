package gov.hhs.gsrs.invitropharmacology;

import gov.hhs.gsrs.invitropharmacology.controllers.InvitroPharmacologyController;
import gov.hhs.gsrs.invitropharmacology.LegacyInvitroPharmacologySearcher;
import gov.hhs.gsrs.invitropharmacology.services.InvitroPharmacologyEntityService;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class InvitroPharmacologySelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                InvitroPharmacologyEntityService.class.getName(),
                LegacyInvitroPharmacologySearcher.class.getName(),
                InvitroPharmacologyController.class.getName()
        };
    }
}
