package gov.hhs.gsrs.invitropharmacology.validators;

import gov.hhs.gsrs.invitropharmacology.models.*;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

public class RequiredFieldNonNullValidator implements ValidatorPlugin<InvitroAssayInformation> {

    @Override
    public boolean supports(InvitroAssayInformation newValue, InvitroAssayInformation oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return true;
    }

    @Override
    public void validate(InvitroAssayInformation objnew, InvitroAssayInformation objold, ValidatorCallback callback) {

        /*
        if (objnew.invitroAssaySets.size() == 0) {
            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("Assay Set is required."));
        }

        if ((objnew.externalAssayId == null) || (objnew.externalAssayId.isEmpty())) {
            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("External Assay ID is required"));
        }

        if (objnew.externalAssaySource == null || objnew.externalAssaySource.isEmpty()) {
            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("External Assay Source is required."));
        }

        if (objnew.targetName == null || objnew.targetName.isEmpty()) {
            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("Target Name is required."));
        }

        if (objnew.invitroAssayScreenings.size() > 0) {
            for (int i = 0; i < objnew.invitroAssayScreenings.size(); i++) {

                InvitroAssayScreening screening = objnew.invitroAssayScreenings.get(i);
                if (screening != null) {
                    if (screening.invitroAssayResultInformation != null) {

                        // Validate Reference
                        if (screening.invitroAssayResultInformation.invitroReferences.size() == 0) {
                            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("Reference is required."));
                        } else if (screening.invitroAssayResultInformation.invitroReferences.size() > 0) {
                            for (int j = 0; j < screening.invitroAssayResultInformation.invitroReferences.size(); j++) {
                                InvitroReference reference = screening.invitroAssayResultInformation.invitroReferences.get(j);
                                if (reference != null) {
                                    if (reference.sourceType == null) {
                                        callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("Reference Source Type is required."));
                                    }
                                }
                            } // for references
                        } // else if
                    } // if invitroAssayResultInformation exists
                } // if screening exist
            } // for screening
        } */
    }
}
