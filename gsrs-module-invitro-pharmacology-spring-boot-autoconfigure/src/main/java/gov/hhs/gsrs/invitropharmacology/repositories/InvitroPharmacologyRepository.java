package gov.hhs.gsrs.invitropharmacology.repositories;

import gov.hhs.gsrs.invitropharmacology.models.*;

import gsrs.repository.GsrsVersionedRepository;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface InvitroPharmacologyRepository extends GsrsVersionedRepository<InvitroAssayInformation, Long> {

    Optional<InvitroAssayInformation> findById(Long id);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssayScreenings s where a.id = ?1")
    List<InvitroAssayInformation> findAllScreeningsByAssayId(Long id);

    @Query("select a from InvitroAssayInformation a WHERE a.targetNameApprovalId = ?1")
    List<InvitroAssayInformation> findAssayByTargetNameApprovalId(String targetNameApprovalId);

    @Query("select a from InvitroAssayInformation a")
    List<InvitroAssayInformation> findAssayByTestAgent(String targetName);

    @Query("select a from InvitroAssayInformation a")
    List<InvitroAssayInformation> findAllScreeningTestAgents();

    @Query("select a from InvitroAssayScreening a JOIN a.invitroAssayResultInformation i where a.screeningImportFileName = ?1")
    List<InvitroAssayScreening> findScreeningByImportFile(String importFilename);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssayScreenings s JOIN s.invitroAssayResult r JOIN s.invitroAssayResultInformation ai where ai.id = ?1")
    List<InvitroAssayInformation> findAssayByResultInfoId(Long resultInfoId);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssayScreenings s JOIN s.invitroSummary su JOIN s.invitroAssayResultInformation ri JOIN ri.invitroTestAgent t where t.testAgent = ?1")
    List<InvitroAssayInformation> findAllScreeningSummaryByTestAgentId(String testAgent);

    @Query("select a from InvitroReference a")
    List<InvitroReference> findAllReferences();

    @Query("select a from InvitroAssayResultInformation a where a.id = ?1")
    InvitroAssayResultInformation findAssayResultInformationById(Long id);

    @Query("select a from InvitroSponsor a")
    List<InvitroSponsor> findAllSponsors();

    @Query("select a from InvitroSponsorSubmitter a")
    List<InvitroSponsorSubmitter> findAllSponsorSubmitters();

    @Query("select a from InvitroLaboratory a")
    List<InvitroLaboratory> findAllLaboratories();

    @Query("select a from InvitroTestAgent a")
    List<InvitroTestAgent> findAllTestAgents();

    // NEED THIS
    @Query("select a from InvitroAssayInformation a order by a.modifiedDate desc")
    List<InvitroAssayInformation> findAllAssays();

    @Query("select a from InvitroAssayInformation a where a.externalAssaySource = ?1 and a.externalAssayId = ?2")
    InvitroAssayInformation findAssayByExternalAssay(String externalAssaySource, String externalAssayId);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssaySets s where s.assaySet = ?1")
    List<InvitroAssayInformation> findAllAssysByAssaySet(String assaySet);

    @Query("select a from InvitroAssaySet a")
    List<InvitroAssaySet> findAllAssaySets();

    @Query("select a from InvitroAssaySet a where a.id = ?1")
    InvitroAssaySet findAssaySetById(Long id);

    @Query("select a from InvitroAssaySet a where a.assaySet = ?1")
    InvitroAssaySet findAssaySetByAssaySet(String assaySet);
}
