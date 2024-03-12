package gov.hhs.gsrs.invitropharmacology;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyInvitroPharmacologySearcher extends LegacyGsrsSearchService<InvitroAssayInformation> {

    @Autowired
    public LegacyInvitroPharmacologySearcher(InvitroPharmacologyRepository repository) {
        super(InvitroAssayInformation.class, repository);
    }
}
