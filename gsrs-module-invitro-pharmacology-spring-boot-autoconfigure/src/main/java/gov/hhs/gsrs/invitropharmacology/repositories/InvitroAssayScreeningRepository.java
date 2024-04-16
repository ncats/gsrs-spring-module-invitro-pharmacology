package gov.hhs.gsrs.invitropharmacology.repositories;

import gov.hhs.gsrs.invitropharmacology.models.*;

import gsrs.repository.GsrsVersionedRepository;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public interface InvitroAssayScreeningRepository extends GsrsVersionedRepository<InvitroAssayScreening, Long> {

    Optional<InvitroAssayScreening> findById(Long id);
}
