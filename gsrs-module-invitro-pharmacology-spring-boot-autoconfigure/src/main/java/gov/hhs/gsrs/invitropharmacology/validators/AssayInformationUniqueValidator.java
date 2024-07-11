package gov.hhs.gsrs.invitropharmacology.validators;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class AssayInformationUniqueValidator implements ValidatorPlugin<InvitroAssayInformation> {

    @Autowired
    private InvitroPharmacologyRepository repository;

    @Override
    public boolean supports(InvitroAssayInformation newValue, InvitroAssayInformation oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return true;
    }

    @Override
    public void validate(InvitroAssayInformation objnew, InvitroAssayInformation objold, ValidatorCallback callback) {

        InvitroAssayInformation assayExists = repository.findAssayByExternalAssay(objnew.externalAssaySource, objnew.externalAssayId);

        // if assay is not null, External Assay Source and External Assay ID already exist in the database. These values should be unique.
        // Create error message when updating a new record
        if (assayExists != null) {
            // When registering a new Assay and assay id is null
            // if Assay ID in the database and this object ID is not equal, then error
            if ((objnew.id == null) || (!assayExists.id.equals(objnew.id))) {
                callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("The combination of External Assay Source and External Assay ID already exist in the database."));
            }
        }
    }

}
