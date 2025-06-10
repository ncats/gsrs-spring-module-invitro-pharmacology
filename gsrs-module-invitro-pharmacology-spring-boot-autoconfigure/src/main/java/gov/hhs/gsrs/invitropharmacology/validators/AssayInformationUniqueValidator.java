package gov.hhs.gsrs.invitropharmacology.validators;

import gov.hhs.gsrs.invitropharmacology.InvitroPharmacologyDataSourceConfig;
import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;

import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidatorCallback;
import ix.ginas.utils.validation.ValidatorPlugin;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

public class AssayInformationUniqueValidator implements ValidatorPlugin<InvitroAssayInformation> {

    @PersistenceContext(unitName = InvitroPharmacologyDataSourceConfig.NAME_ENTITY_MANAGER)
    private EntityManager entityManager;

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

        // Re-attach the detach object
        // For @ManyToOne and @ManyToMany, when passing a detached child object, need to re-attach it.

        // If the size of the AssaySet is not same for new and old object
        if (objnew.invitroAssaySets != null) {
          //  if (objnew.invitroAssaySets.size() != objold.invitroAssaySets.size()) {

                if (objnew.invitroAssaySets.size() > 0) {

                    for (int i = 0; i < objnew.invitroAssaySets.size(); i++) {
                        InvitroAssaySet asySet = objnew.invitroAssaySets.get(i);
                        // if AssaySet already exists into the database, get the InvitroAssaySet object by Id
                        if (asySet.id != null) {

                            entityManager.merge(asySet);

                            // Find Assay Set By Assay Set id
                            InvitroAssaySet existingAssaySet = repository.findAssaySetById(asySet.id);

                            //if AssaySet Object found in the database, set it to Assay
                            if (existingAssaySet != null) {
                                asySet = existingAssaySet;
                                objnew.invitroAssaySets.set(i, asySet);
                            }

                        }
                    }
                }
           // }
        }

        // Re-attach Invitro Assay Result Information that already exists into the new Screening object.
        if (objnew.invitroAssayScreenings != null) {
            if (objnew.invitroAssayScreenings.size() > 0) {

                for (int j = 0; j < objnew.invitroAssayScreenings.size(); j++) {
                    InvitroAssayScreening screening = objnew.invitroAssayScreenings.get(j);

                    if (screening.id == null) {
                        if (screening.invitroAssayResultInformation != null) {

                            if (screening.invitroAssayResultInformation.id != null) {

                                entityManager.merge(screening.invitroAssayResultInformation);

                                // Find Information Result by Id
                                InvitroAssayResultInformation resultInfo = repository.findAssayResultInformationById(screening.invitroAssayResultInformation.id);

                                //if AssaySet Object found in the database, set it to Assay
                                if (resultInfo != null) {
                                    screening.invitroAssayResultInformation = resultInfo;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
