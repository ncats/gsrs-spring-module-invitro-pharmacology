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

    // @Query("select a from InvitroAssayScreening a WHERE (a.assayTargetUnii = ?1) OR (a.testCompoundUnii = ?1) OR (a.ligandSubstrateUnii = ?1) OR (a.controlUnii = ?1)")

    // @Query("select a from InvitroAssayScreening a JOIN a.invitroTestCompound t WHERE (a.assayTargetUnii = ?1) OR (a.ligandSubstrateUnii = ?1) OR (a.controlUnii = ?1) OR (t.testCompoundUnii = ?1)")
    // @Query("select a from InvitroAssayScreening a WHERE (a.assayTargetUnii = ?1) OR (a.ligandSubstrateUnii = ?1) OR (a.controlUnii = ?1) OR (a.testAgentUnii = ?1)")
    @Query("select a from InvitroAssayInformation a WHERE a.targetNameApprovalId = ?1")
    List<InvitroAssayInformation> findAssayByTargetNameApprovalId(String targetNameApprovalId);

    //@Query("select a from InvitroAssayInformation a JOIN a.invitroInformationReferences ir JOIN ir.invitroAssayInfoRefTestAgents WHERE a.id= ?1")
    // @Query("select a from InvitroAssayInformation a WHERE a.targetName= ?1")
    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssayScreenings s JOIN s.invitroTestAgent ta WHERE ta.testAgent = ?1")
    List<InvitroAssayInformation> findAssayByTestAgent(String targetName);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssaySets s where s.assaySet = ?1")
    List<InvitroAssayInformation> findAllAssysByAssaySet(String assaySet);

    @Query("select a from InvitroAssayInformation a JOIN a.invitroAssayScreenings s JOIN s.invitroTestAgent ta where ta.testAgent IS NOT NULL")
    List<InvitroAssayInformation> findAllScreeningTestAgents();

    @Query("select a from InvitroAssayInformation a where a.externalAssaySource = ?1 and a.externalAssayId = ?2")
    InvitroAssayInformation findAssayByExternalAssay(String externalAssaySource, String externalAssayId);

    @Query("select a from InvitroAssayInformation a")
    List<InvitroAssayInformation> findAllAssays();

    @Query("select a from InvitroAssaySet a")
    List<InvitroAssaySet> findAllAssaySets();

    @Query("select a from InvitroReference a")
    List<InvitroReference> findAllReferences();

    @Query("select a from InvitroSponsor a")
    List<InvitroSponsor> findAllSponsors();

    @Query("select a from InvitroSponsorSubmitter a")
    List<InvitroSponsorSubmitter> findAllSponsorSubmitters();

    @Query("select a from InvitroLaboratory a")
    List<InvitroLaboratory> findAllLaboratories();

    @Query("select a from InvitroTestAgent a")
    List<InvitroTestAgent> findAllTestAgents();

}
