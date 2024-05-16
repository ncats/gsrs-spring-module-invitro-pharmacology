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

import javax.persistence.EntityManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class InvitroAssayScreeningEntityService extends AbstractGsrsEntityService<InvitroAssayScreening, Long> {
    public static final String  CONTEXT = "assayscreening";

    public InvitroAssayScreeningEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private InvitroAssayScreeningRepository repository;

    @Autowired
    private InvitroAssayScreeningRepository assayScreeningRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<InvitroAssayScreening> getEntityClass() {
        return InvitroAssayScreening.class;
    }

    @Override
    public Long parseIdFromString(String idAsString) {
        return Long.parseLong(idAsString);
    }

    @Override
    protected InvitroAssayScreening fromNewJson(JsonNode json) throws IOException {
        return objectMapper.convertValue(json, InvitroAssayScreening.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected InvitroAssayScreening create(InvitroAssayScreening assayInfo) {
        try {
          /*  if (assayInfo.assayId == null) {
                assayInfo.assayId = UUID.randomUUID().toString();
            }*/
            return repository.saveAndFlush(assayInfo);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected InvitroAssayScreening update(InvitroAssayScreening assayInfo) {
        return repository.saveAndFlush(assayInfo);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<InvitroAssayScreening> newUpdateEvent(InvitroAssayScreening updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<InvitroAssayScreening> newCreationEvent(InvitroAssayScreening createdEntity) {
        return null;
    }

    @Override
    public Long getIdFrom(InvitroAssayScreening entity) {
        return entity.getId();
    }

    @Override
    protected List<InvitroAssayScreening> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected InvitroAssayScreening fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, InvitroAssayScreening.class);
    }

    @Override
    protected List<InvitroAssayScreening> fromUpdatedJsonList(JsonNode list) throws IOException {
       return null;
    }

    @Override
    protected JsonNode toJson(InvitroAssayScreening details) throws IOException {
        return objectMapper.valueToTree(details);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<InvitroAssayScreening> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<InvitroAssayScreening> flexLookup(String someKindOfId) {
        if (someKindOfId == null){
            return Optional.empty();
        }
        return repository.findById(Long.parseLong(someKindOfId));
    }

    @Override
    protected Optional<Long> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<InvitroAssayScreening> found = flexLookup(someKindOfId);
        if(found.isPresent()){
            return Optional.of(found.get().id);
        }
        return Optional.empty();
    }

    @Transactional
    public InvitroAssayScreening saveBulkAssays(InvitroAssayScreening assayInfo, EntityManager entityManager) {
        return this.update(assayInfo);
    }

    @Transactional
    public InvitroAssayScreening updateBulkScreenings(InvitroAssayScreening screening, EntityManager entityManager) {
        try {
            InvitroAssayScreening obj = null;

            assayScreeningRepository.saveAndFlush(screening);

            return obj;
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

}
