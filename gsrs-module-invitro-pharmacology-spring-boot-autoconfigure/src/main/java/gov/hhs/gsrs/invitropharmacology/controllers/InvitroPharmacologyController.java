package gov.hhs.gsrs.invitropharmacology.controllers;

import gov.hhs.gsrs.invitropharmacology.InvitroPharmacologyDataSourceConfig;
import gov.hhs.gsrs.invitropharmacology.SubstanceModuleService;
import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.LegacyInvitroPharmacologySearcher;
import gov.hhs.gsrs.invitropharmacology.services.InvitroPharmacologyEntityService;

import gov.nih.ncats.common.util.Unchecked;
import gsrs.DefaultDataSourceConfig;
import gsrs.GsrsFactoryConfiguration;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.controller.hateoas.HttpRequestHolder;
import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.module.substance.SubstanceEntityServiceImpl;
import gsrs.repository.ETagRepository;
import gsrs.service.EtagExportGenerator;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;
import ix.core.models.ETag;
import ix.core.search.SearchOptions;
import ix.core.search.text.TextIndexer;
import ix.ginas.exporters.ExportMetaData;
import ix.ginas.exporters.ExportProcess;
import ix.ginas.exporters.Exporter;
import ix.ginas.exporters.ExporterFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import net.minidev.json.JSONObject;

@Slf4j
@ExposesResourceFor(InvitroAssayInformation.class)
@GsrsRestApiController(context = InvitroPharmacologyEntityService.CONTEXT, idHelper = IdHelpers.NUMBER)
public class InvitroPharmacologyController extends EtagLegacySearchEntityController<InvitroPharmacologyController, InvitroAssayInformation, Long> {

    @Autowired
    private ETagRepository eTagRepository;

    @PersistenceContext(unitName =  InvitroPharmacologyDataSourceConfig.NAME_ENTITY_MANAGER)
    private EntityManager entityManager;
    @Autowired
    private GsrsControllerConfiguration gsrsControllerConfiguration;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private ExportService exportService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private GsrsExportConfiguration gsrsExportConfiguration;


    @Autowired
    private InvitroPharmacologyEntityService invitroPharmacologyEntityService;

    @Autowired
    private SubstanceModuleService substanceModuleService;

    @Autowired
    private LegacyInvitroPharmacologySearcher legacyInvitroPharmacologySearcher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GsrsFactoryConfiguration gsrsFactoryConfiguration;


    @Override
    public GsrsEntityService<InvitroAssayInformation, Long> getEntityService() {
        return invitroPharmacologyEntityService;
    }

    @Override
    protected LegacyGsrsSearchService<InvitroAssayInformation> getlegacyGsrsSearchService() {
        return legacyInvitroPharmacologySearcher;
    }

    @Override
    protected Stream<InvitroAssayInformation> filterStream(Stream<InvitroAssayInformation> stream, boolean publicOnly, Map<String, String> parameters) {
        return stream;
    }

    @GetGsrsRestApiMapping("/actuator/health")
    public ResponseEntity<Object> checkHealth() throws Exception {
        JSONObject status = new JSONObject();
        status.put("status", "UP");
        return new ResponseEntity(status, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assay/{id}/screenings")
    public ResponseEntity<String> findAllScreeningsByAssayId(@PathVariable("id") Long assayId) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAllScreeningsByAssayId(assayId);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutGsrsRestApiMapping("/saveBulkAssays")
    //@Transactional
    public ResponseEntity<Object> updateEntity(@RequestBody JsonNode[] updatedEntityJson,
                                               @RequestParam Map<String, String> queryParameters,
                                               Principal principal) throws Exception {
        if (principal == null) {
            //not logged in!
            return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }

        List<InvitroAssayInformation> assayInfos = new ArrayList<>(updatedEntityJson.length);
        System.out.println("******************************************** ");
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < updatedEntityJson.length; i++) {
            System.out.println("**************** " + updatedEntityJson[i]);

            InvitroAssayInformation assayInfo = mapper.treeToValue(updatedEntityJson[i], InvitroAssayInformation.class);
           // assayInfos.add(assayInfo);
           invitroPharmacologyEntityService.saveBulkAssays(assayInfo);
        }


          //  List<T> l = new ArrayList<>(updatedEntityJson.size());
       // JsonNode node = mapper.valueToTree(updatedEntityJson);
      //  InvitroAssayInformation value = mapper.treeToValue(updatedEntityJson, InvitroAssayInformation.class);

            /*for(JsonNode n : updatedEntityJson){
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
                System.out.println(n);
            }*/

        /*
        AbstractGsrsEntityService.UpdateResult<T> result = getEntityService().updateEntity(updatedEntityJson);

        if(result.getStatus()== AbstractGsrsEntityService.UpdateResult.STATUS.NOT_FOUND){
            return gsrsControllerConfiguration.handleNotFound(queryParameters);
        }

        if(result.getStatus()== AbstractGsrsEntityService.UpdateResult.STATUS.ERROR){
            return new ResponseEntity<>(result.getValidationResponse(),gsrsControllerConfiguration.getHttpStatusFor(HttpStatus.BAD_REQUEST, queryParameters));
        }
        */
        return gsrsControllerConfiguration.handleNotFound(queryParameters);
        //return new ResponseEntity(list, HttpStatus.OK);
    }

    public ResponseEntity<String> findAllAssays(@PathVariable("assayTargetUnii") String assayTargetUnii) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAllAssays();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allAssays")
    public ResponseEntity<String> findAllAssays() throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAllAssays();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assaysByAssaySets/{assaySet}")
    public ResponseEntity<String> findAllAssysByAssaySet(@PathVariable("assaySet") String assaySet) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAllAssysByAssaySet(assaySet);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allAssaySets")
    public ResponseEntity<String> findAllAssaySets() throws Exception {
        List<InvitroAssaySet> list = invitroPharmacologyEntityService.findAllAssaySets();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allReferences")
    public ResponseEntity<String> findAllReferences() throws Exception {
        List<InvitroReference> list = invitroPharmacologyEntityService.findAllReferences();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allSponsors")
    public ResponseEntity<String> findAllSponsors() throws Exception {
        List<InvitroSponsor> list = invitroPharmacologyEntityService.findAllSponsors();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allSponsorSubmitters")
    public ResponseEntity<String> findAllSponsorSubmitters() throws Exception {
        List<InvitroSponsorSubmitter> list = invitroPharmacologyEntityService.findAllSponsorSubmitters();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allLaboratories")
    public ResponseEntity<String> findAllLaboratories() throws Exception {
        List<InvitroLaboratory> list = invitroPharmacologyEntityService.findAllLaboratories();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/allTestAgents")
    public ResponseEntity<String> findAllTestAgents() throws Exception {
        List<InvitroTestAgent> list = invitroPharmacologyEntityService.findAllTestAgents();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assaytargetunii/{assayTargetUnii}")
    public ResponseEntity<String> findAssayByTargetNameApprovalId(@PathVariable("assayTargetUnii") String assayTargetUnii) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAssayByTargetNameApprovalId(assayTargetUnii);
        if (assayTargetUnii == null) {
            throw new IllegalArgumentException("There is no Assay Target Unii provided");
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assayTestAgent/{assayTestAgent}")
    public ResponseEntity<String> findAssayByTestAgent(@PathVariable("assayTestAgent") String assayTestAgent) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAssayByTestAgent(assayTestAgent);
        if (assayTestAgent == null) {
            throw new IllegalArgumentException("There is no Assay Test Agent provided");
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
