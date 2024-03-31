package gov.hhs.gsrs.invitropharmacology.validators;

import gov.hhs.gsrs.invitropharmacology.models.*;

import gov.hhs.gsrs.invitropharmacology.repositories.*;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

import org.springframework.beans.factory.annotation.Autowired;

public class AssayInformationUniqueValidator implements ValidatorPlugin<InvitroAssayInformation> {

    @Autowired
    private InvitroPharmacologyRepository repository;

    @Override
    public boolean supports(InvitroAssayInformation newValue, InvitroAssayInformation oldValue, ValidatorConfig.METHOD_TYPE methodType) {
        return true;
    }

    @Override
    public void validate(InvitroAssayInformation objnew, InvitroAssayInformation objold, ValidatorCallback callback) {

        System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZ");
        System.out.println("***** " + objnew.externalAssaySource + "      " +  objnew.externalAssayId);
        InvitroAssayInformation assay = repository.findAssayByExternalAssay(objnew.externalAssaySource, objnew.externalAssayId);

        if (assay != null) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
            callback.addMessage(GinasProcessingMessage.ERROR_MESSAGE("External Assay Source and External Assay ID should be unique."));
        } else {
            System.out.println("BBBBBBBBBBBBBBBBBBBBBB");
        }
    }
}
