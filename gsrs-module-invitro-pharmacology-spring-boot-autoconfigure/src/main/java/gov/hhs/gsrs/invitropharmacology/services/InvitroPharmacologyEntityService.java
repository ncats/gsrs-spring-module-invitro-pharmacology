package gov.hhs.gsrs.invitropharmacology.services;

import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.repositories.*;

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

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class InvitroPharmacologyEntityService extends AbstractGsrsEntityService<InvitroAssayInformation, Long> {
    public static final String  CONTEXT = "invitropharmacology";

    public InvitroPharmacologyEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private InvitroPharmacologyRepository repository;

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
    protected InvitroAssayInformation update(InvitroAssayInformation details) {
        return repository.saveAndFlush(details);
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
        if (someKindOfId == null){
            return Optional.empty();
        }
        return repository.findById(Long.parseLong(someKindOfId));
    }

    @Override
    protected Optional<Long> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<InvitroAssayInformation> found = flexLookup(someKindOfId);
        if(found.isPresent()){
            return Optional.of(found.get().id);
        }
        return Optional.empty();
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

    public List<InvitroAssayInformation> findAssayByTargetNameApprovalId(String assayTargetUnii) {
        List<InvitroAssayInformation> list = repository.findAssayByTargetNameApprovalId(assayTargetUnii);
        return list;
    }

    public List<InvitroAssayInformation> findAssayByTestAgent(String assayTestAgent) {
        List<InvitroAssayInformation> list = repository.findAssayByTestAgent(assayTestAgent);
        return list;
    }

}
