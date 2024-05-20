package gov.hhs.gsrs.invitropharmacology.processors;

import gov.hhs.gsrs.invitropharmacology.controllers.InvitroPharmacologyController;
import gov.hhs.gsrs.invitropharmacology.models.*;

import ix.core.EntityProcessor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class InvitroAssayScreeningProcessor implements EntityProcessor<InvitroAssayInformation> {

    @Autowired
    public InvitroPharmacologyController invitroPharmacologyController;

    @Override
    public Class<InvitroAssayInformation> getEntityClass() {
        return InvitroAssayInformation.class;
    }

    @Override
    public void prePersist(final InvitroAssayInformation obj) {
    }

    @Override
    public void preUpdate(InvitroAssayInformation obj) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@ PRE PRE PROCESSOR");
    }

    @Override
    public void preRemove(InvitroAssayInformation obj) {
    }

    @Override
    public void postLoad(InvitroAssayInformation obj) throws FailProcessingException {
    }

}

