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
@Transactional
public class InvitroPharmacologyEntityService extends AbstractGsrsEntityService<InvitroAssayInformation, Long> {
    public static final String  CONTEXT = "invitropharmacology";

    public InvitroPharmacologyEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private InvitroPharmacologyRepository repository;

    @Autowired
    private InvitroAssayScreeningRepository assayScreeningRepository;

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

    @Transactional
    public InvitroAssayInformation saveBulkAssays(InvitroAssayInformation assayInfo) {
        return this.update(assayInfo);
    }

    @Transactional
    public InvitroAssayScreening updateBulkScreenings(InvitroAssayScreening screening, EntityManager entityManager) {
        InvitroAssayScreening obj = null;
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            // screening.internalVersion = 1L;

            //first bump version?
          //  screening.forceUpdate();

          //  assayScreeningRepository.save(entityManager.merge(screening));

           // screening.forceUpdate();

          //  assayScreeningRepository.save(entityManager.merge(screening));


         //   Optional<InvitroAssayScreening> a2 = assayScreeningRepository.findById(63L);
         //   System.out.println("FOUND SCREENING " + a2);

            Optional<InvitroAssayInformation> ai  = get(1L);
           // Optional<InvitroAssayInformation> ai = repository.findById(1L);
            if(ai.isPresent()){
                System.out.println("INFORMATION FOUND FOUND FOUND " + Optional.of(ai.get().id));
                InvitroAssayInformation abc = ai.get();
                abc.assayMode = "BBBBBBBBBBBBBBB";
                abc.publicDomain = "NOOOOOOOOOOOOOO";


          //  repository.saveAndFlush(ai.get());
            update(abc);
            }

          //  System.out.println("******* CONTAINS INFORMATION" + entityManager.contains(ai.get()));


         //   System.out.println("******* CONTAINS A2 A2" + entityManager.contains(a2.get()));
//
            /*
            if (ai.isPresent()) {
                System.out.println("FOUND " + ai);

               // screening.owner = ai.get();
               // entityManager.persist(screening);
              //  entityManager.merge(screening);
             //   entityManager.getTransaction().commit();
            } */

          //  a2.get().testing = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA";
         //   System.out.println("******* CONTAINS A2 A2" + entityManager.contains(a2.get()));

            // FOR SAVING NEW RECORD BEGIN
              if (screening.id == null) {
                  /*
                  System.out.println("################# SAVING NEW RECORD ############");
                  screening.forceUpdate();
                  assayScreeningRepository.saveAndFlush(screening);  */
              } else {

               //   System.out.println("******* CONTAINS " + entityManager.contains(screening));
                //  System.out.println("&&&&&&&&&&&&&&&&&&&&&& UPDATING RECORD ############");

                //  screening.forceUpdate();
                //  assayScreeningRepository.saveAndFlush(entityManager.merge(screening));

                 /*
                  InvitroAssayScreening name2 = entityManager.merge(a2.get());
                  name2.testing = "GGGGGGGGGGGGGGGGGGGGGGGGG";
                  // log.trace("name2 dirtiness: {}" , (log.isTraceEnabled()) ? name2.isDirty(): "");
                  name2.forceUpdate();
                 // log.trace("name2 dirtiness after update: {}", (log.isTraceEnabled()) ? name2.isDirty() : "");
                  assayScreeningRepository.saveAndFlush(name2);*/
              }
            // SAVE END


            //  screening.save(screening);
          //  entityManager.persist(screening);
           // entityManager.flush();
            System.out.println("\n");

         //   System.out.println("!!!!!!!! " + screening);

         //   Optional<InvitroAssayScreening> sc = assayScreeningRepository.findById(1L);
          //  System.out.println("############################ " + sc.get());

           // entityManager.getTransaction();
            //InvitroAssayScreening screen = entityManager.merge(screening);
         //   screening.forceUpdate();
           // InvitroAssayScreening savedObj = assayScreeningRepository.saveAndFlush(screening);


           // InvitroAssayScreening obj = assayScreeningRepository.saveAndFlush(screening);
            System.out.println("******* SAVING " + obj);

        } catch (Exception t) {
            t.printStackTrace();
           // throw t;
        }
        return obj;
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
        System.out.println("*********************************************");
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
