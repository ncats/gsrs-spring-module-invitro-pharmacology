package gov.hhs.gsrs.invitropharmacology;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyAssayScreeningSearcher extends LegacyGsrsSearchService<InvitroAssayScreening> {

    @Autowired
    public LegacyAssayScreeningSearcher(InvitroAssayScreeningRepository repository) {
        super(InvitroAssayScreening.class, repository);
    }
}
