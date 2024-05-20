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
    }
}
