package gov.hhs.gsrs.invitropharmacology.services;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;
import gov.hhs.gsrs.invitropharmacology.InvitroPharmacologyDataSourceConfig;

import gsrs.controller.IdHelpers;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class InvitroPharmacologyEntityService extends AbstractGsrsEntityService<InvitroAssayInformation, Long> {
    public static final String CONTEXT = "invitropharmacology";

    public InvitroPharmacologyEntityService() {
        super(CONTEXT, IdHelpers.NUMBER, null, null, null);
    }

    @PersistenceContext(unitName = InvitroPharmacologyDataSourceConfig.NAME_ENTITY_MANAGER)
    private EntityManager entityManager;

    @Autowired
    private InvitroPharmacologyRepository repository;

    @Autowired
    private InvitroAssayScreeningRepository assayScreeningRepository;

    @Autowired
    private InvitroAssayScreeningEntityService invitroAssayScreeningEntityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<InvitroAssayInformation> getEntityClass() {
        return InvitroAssayInformation.class;
    }

    @Override
    public Long parseIdFromString(String idAsString) {
        return Long.parseLong(idAsString);
    }

    @Override
    protected InvitroAssayInformation fromNewJson(JsonNode json) throws IOException {
        return objectMapper.convertValue(json, InvitroAssayInformation.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected InvitroAssayInformation create(InvitroAssayInformation assayInfo) {
        try {
            if (assayInfo.assayId == null) {
                assayInfo.assayId = UUID.randomUUID().toString();
            }
            return repository.saveAndFlush(assayInfo);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected InvitroAssayInformation update(InvitroAssayInformation assayInfo) {
        int size = assayInfo.invitroAssayScreenings.size();
        if (size > 0) {

            InvitroAssayScreening screening = assayInfo.invitroAssayScreenings.get(size - 1);

            String importFileName = screening.screeningImportFileName;

            if (importFileName != null) {
                List<InvitroAssayScreening> screeningImportFile = repository.findScreeningByImportFile(importFileName);

                System.out.println("$$$$$$$$$$$$$$$ " + screeningImportFile);
                // Found in the Database
                if (screeningImportFile != null && screeningImportFile.size() > 0) {
                    System.out.println("HHHHHHHHHHHHHHHHHHHH");
                    if (screeningImportFile.get(0) != null) {
                        System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMM");
                        if (screeningImportFile.get(0).invitroAssayResultInformation != null) {
                            System.out.println("RRRRRRRRRRRRRRRRRRRRR");
                            if (screeningImportFile.get(0).invitroAssayResultInformation.id != null) {
                                System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKK");
                                Long resultInfoId = screeningImportFile.get(0).invitroAssayResultInformation.id;
                                System.out.println("REF INFO ID " + resultInfoId);

                                InvitroAssayResultInformation resultInfo = repository.findAssayResultInformationById(resultInfoId);

                                System.out.println("FOUND FOUND " + resultInfo);
                                screening.setInvitroAssayResultInformation(resultInfo);
                                assayInfo.invitroAssayScreenings.set(size - 1, screening);
                            }
                        }
                    }

                } else {
                    screening.setInfoDirty();
                }
            }
        } // if size > 0

        return repository.saveAndFlush(assayInfo);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<InvitroAssayInformation> newUpdateEvent(InvitroAssayInformation updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<InvitroAssayInformation> newCreationEvent(InvitroAssayInformation createdEntity) {
        return null;
    }

    @Override
    public Long getIdFrom(InvitroAssayInformation entity) {
        return entity.getId();
    }

    @Override
    protected List<InvitroAssayInformation> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected InvitroAssayInformation fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, InvitroAssayInformation.class);
    }

    @Override
    protected List<InvitroAssayInformation> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected JsonNode toJson(InvitroAssayInformation details) throws IOException {
        return objectMapper.valueToTree(details);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<InvitroAssayInformation> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<InvitroAssayInformation> flexLookup(String someKindOfId) {
        if (someKindOfId == null) {
            return Optional.empty();
        }
        return repository.findById(Long.parseLong(someKindOfId));
    }

    @Override
    protected Optional<Long> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<InvitroAssayInformation> found = flexLookup(someKindOfId);
        if (found.isPresent()) {
            return Optional.of(found.get().id);
        }
        return Optional.empty();
    }

    @Transactional
    public InvitroAssayInformation saveBulkAssays(InvitroAssayInformation assayInfo) {
        return this.update(assayInfo);
    }

    @Transactional
    public void saveScreeningRef(EntityManager entityManager) {

        Optional<InvitroAssayScreening> databaseScreening = assayScreeningRepository.findById(241L);

        if (databaseScreening.get() != null) {
            invitroAssayScreeningEntityService.update(databaseScreening.get());

            InvitroAssayScreening saved = assayScreeningRepository.saveAndFlush(databaseScreening.get());
        }
    }

    @Transactional
    public InvitroAssayScreening saveScreening(InvitroAssayScreening screening, Long assayId, EntityManager entityManager) {
        InvitroAssayScreening obj = null;

        try {
            // if Assay Id is not null, get the Assay Record by Id
            if (assayId != null) {
                Optional<InvitroAssayInformation> assay = repository.findById(assayId);
                if (assay.isPresent()) {
                    // set the returned assay as a owner of the screening record
                    screening.setOwner(assay.get());
                }
            }
            //  InvitroAssayScreening screening2 = null;
            //  InvitroAssayScreening newscreening = null;
            // if Result Information object is null, get the id
            if (screening.invitroAssayResultInformation != null) {
                if (screening.invitroAssayResultInformation.id != null) {

                    InvitroAssayResultInformation resultInfo = repository.findAssayResultInformationById(screening.invitroAssayResultInformation.id);

                    resultInfo.addInvitroAssayScreeningChild(screening);

                    screening.setInvitroAssayResultInformation(resultInfo);
                    screening.setIsDirty("invitroAssayInformation");

                    entityManager.joinTransaction();
                    entityManager.merge(screening);
                } else {
                    return assayScreeningRepository.saveAndFlush(screening);

                }
            }

        } catch (Exception t) {
            t.printStackTrace();
        }
        return obj;
    }

    @Transactional
    public InvitroAssayScreening updateBulkScreenings(InvitroAssayScreening screening, EntityManager entityManager) {
        InvitroAssayScreening obj = null;
        try {
            if (screening.id == null) {
            } else {

            }
        } catch (Exception t) {
            t.printStackTrace();
            // throw t;
        }
        return obj;
    }

    public AbstractGsrsEntityService.UpdateResult<InvitroAssayInformation> updateAssayForScreening(InvitroAssayInformation assayInformation, Long resultInfoId, EntityManager entityManager) {
        AbstractGsrsEntityService.UpdateResult.UpdateResultBuilder<InvitroAssayInformation> builder = AbstractGsrsEntityService.UpdateResult.<InvitroAssayInformation>builder();

        try {

            int size = assayInformation.invitroAssayScreenings.size();
            if (size > 0) {
                InvitroAssayScreening screening = assayInformation.invitroAssayScreenings.get(size - 1);

            } // if size > 0

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        builder.updatedEntity(update(assayInformation));

        builder.status(UpdateResult.STATUS.UPDATED);

        UpdateResult<InvitroAssayInformation> updateResult = builder.build();

        return updateResult;

    }

    public InvitroAssayInformation findAssayByExternalAssay(String externalAssaySource, String externalAssayId) {
        InvitroAssayInformation assay = repository.findAssayByExternalAssay(externalAssaySource, externalAssayId);
        return assay;
    }

    public List<InvitroAssayInformation> findAssayByResultInfoId(Long id) {
        List<InvitroAssayInformation> list = repository.findAssayByResultInfoId(id);
        return list;
    }

    public List<InvitroAssayInformation> findAllScreeningsByAssayId(Long id) {
        List<InvitroAssayInformation> list = repository.findAllScreeningsByAssayId(id);
        return list;
    }

    public List<InvitroAssayInformation> findAllAssays() {
        List<InvitroAssayInformation> list = repository.findAllAssays();
        return list;
    }

    public List<InvitroAssayInformation> findAllAssysByAssaySet(String assaySet) {
        List<InvitroAssayInformation> list = repository.findAllAssysByAssaySet(assaySet);
        return list;
    }

    public List<InvitroAssaySet> findAllAssaySets() {
        List<InvitroAssaySet> list = repository.findAllAssaySets();
        return list;
    }

    public List<InvitroReference> findAllReferences() {
        List<InvitroReference> list = repository.findAllReferences();
        return list;
    }

    public List<InvitroSponsor> findAllSponsors() {
        List<InvitroSponsor> list = repository.findAllSponsors();
        return list;
    }

    public List<InvitroSponsorSubmitter> findAllSponsorSubmitters() {
        List<InvitroSponsorSubmitter> list = repository.findAllSponsorSubmitters();
        return list;
    }

    public List<InvitroLaboratory> findAllLaboratories() {
        List<InvitroLaboratory> list = repository.findAllLaboratories();
        return list;
    }

    public List<InvitroTestAgent> findAllTestAgents() {
        List<InvitroTestAgent> list = repository.findAllTestAgents();
        return list;
    }

    public List<InvitroAssayScreening> findAllScreeningSummaryByTestAgentId(Long TestAgentId) {
        List<InvitroAssayScreening> list = repository.findAllScreeningSummaryByTestAgentId(TestAgentId);
        return list;
    }


    public List<InvitroAssayInformation> findAssayByTargetNameApprovalId(String assayTargetUnii) {
        List<InvitroAssayInformation> list = repository.findAssayByTargetNameApprovalId(assayTargetUnii);
        return list;
    }

    public List<InvitroAssayInformation> findAssayByTestAgent(String assayTestAgent) {
        List<InvitroAssayInformation> list = repository.findAssayByTestAgent(assayTestAgent);
        return list;
    }

}
