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
import gsrs.service.AbstractGsrsEntityService;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @Autowired
    private InvitroAssayScreeningController invitroAssayScreeningController;


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

   // @PutGsrsRestApiMapping(value="({id})/screening")
    @PostGsrsRestApiMapping("/{assayId}/screening")
    public ResponseEntity<Object> saveScreening(@RequestBody JsonNode entityJson,
                                                @RequestParam Map<String, String> queryParameters,
                                                Principal principal,
                                                @PathVariable("assayId") Long assayId) throws Exception {
        if (principal == null) {
            //not logged in!
            return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }

        ObjectMapper mapper = new ObjectMapper();

        InvitroAssayScreening screening = mapper.treeToValue(entityJson, InvitroAssayScreening.class);

        InvitroAssayScreening savedScreening = invitroPharmacologyEntityService.saveScreening(screening, assayId, entityManager);

        InvitroAssayScreening screeningTesting = new InvitroAssayScreening();
        if (screening.invitroAssayResultInformation != null) {

            if (screening.invitroAssayResultInformation.id != null) {

                return new ResponseEntity(screening, HttpStatus.OK);

            }
        }

        return new ResponseEntity(screening, HttpStatus.OK);

        //   List<InvitroAssayScreening> savedList = new ArrayList<InvitroAssayScreening>();

        //   ObjectMapper mapper = new ObjectMapper();

        //   for (int i = 0; i < updatedEntityJson.length; i++) {
        // System.out.println("**************** " + updatedEntityJson[i]);

        // Convert Json to InvitroAssayScreening Class Object
        //     InvitroAssayScreening screeningObj = mapper.treeToValue(updatedEntityJson[i], InvitroAssayScreening.class);


        // screeningObj.setOwner();
        // Save/Update the InvitroAssayScreening record into the database
            /*
            InvitroAssayScreening savedScreeningObj = invitroPharmacologyEntityService.updateBulkScreenings(screeningObj, entityManager);
            */

        //  InvitroAssayScreening savedScreeningObj = invitroAssayScreeningController.updateBulkScreenings(screeningObj);

        // InvitroAssayScreening savedScreeningObj = invitroPharmacologyEntityService.updateBulkScreenings(screeningObj, entityManager);

        //  if (savedScreeningObj != null) {
        //       savedList.add(savedScreeningObj);
        //   }
        // }
        //  return new ResponseEntity(savedList, HttpStatus.OK);

        // return gsrsControllerConfiguration.handleNotFound(queryParameters);
    }

    @GetGsrsRestApiMapping("/actuator/health")
    public ResponseEntity<Object> checkHealth() throws Exception {
        JSONObject status = new JSONObject();
        status.put("status", "UP");
        return new ResponseEntity(status, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assay/({id})/screenings")
    public ResponseEntity<String> findAllScreeningsByAssayId(@PathVariable("id") Long assayId) throws Exception {
        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAllScreeningsByAssayId(assayId);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/testAgentSummaries/{id}")
    public ResponseEntity<String> findAllScreeningSummaryByTestAgentId(@PathVariable("id") Long testAgentId) throws Exception {

        List<InvitroAssayScreening> list = invitroPharmacologyEntityService.findAllScreeningSummaryByTestAgentId(testAgentId);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/externalAssay/{externalAssaySource}/{externalAssayId}")
    public ResponseEntity<String> findAssayByExternalAssay(@PathVariable("externalAssaySource") String externalAssaySource,
                                                           @PathVariable("externalAssayId") String externalAssayId) throws Exception {

        InvitroAssayInformation assay = invitroPharmacologyEntityService.findAssayByExternalAssay(externalAssaySource, externalAssayId);

        return new ResponseEntity(assay, HttpStatus.OK);
    }

    @GetGsrsRestApiMapping("/assaysByResultInfoId/{id}")
    public ResponseEntity<String> findAssayByResultInfoId(@PathVariable("id") Long refInfoId) throws Exception {

        List<InvitroAssayInformation> list = invitroPharmacologyEntityService.findAssayByResultInfoId(refInfoId);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutGsrsRestApiMapping("/saveAssay")
    public ResponseEntity<Object> saveNewAssay(@RequestBody JsonNode updatedEntityJson,
                                                  @RequestParam Map<String, String> queryParameters,
                                                  Principal principal) throws Exception {
        if (principal == null) {
            //not logged in!
            return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }

        ObjectMapper mapper = new ObjectMapper();

        JsonNode nodeResultInfo = updatedEntityJson.findPath("invitroAssayResultInformation");
        // if no Id found, it is new
        Long resultInfoId = null;
        if (nodeResultInfo != null) {
            resultInfoId = nodeResultInfo.get("id").asLong();


            System.out.println("################### id" + resultInfoId);

          //  if (nodeResultInfo instanceof ObjectNode) {
                System.out.println("YES YES YES");
        //      ((ObjectNode)nodeResultInfo).remove("invitroAssayResultInformation");
             //   ObjectNode object = (ObjectNode) nodeResultInfo;
             //   object.remove("nodeResultInfo");
         //   }
          //  ((ObjectNode)updatedEntityJson).remove("invitroAssayResultInformation"]);
        }

        InvitroAssayInformation assayInfo = mapper.treeToValue(updatedEntityJson, InvitroAssayInformation.class);

        AbstractGsrsEntityService.UpdateResult<InvitroAssayInformation> result = invitroPharmacologyEntityService.updateAssayForScreening(assayInfo,resultInfoId,  entityManager);

        // if Id found and createdBy not found, remove the object
        return new ResponseEntity<>(result.getUpdatedEntity(), HttpStatus.OK);
      //  return new ResponseEntity(savedAssay, HttpStatus.OK);

        // return gsrsControllerConfiguration.handleNotFound(queryParameters);

        //  List<T> l = new ArrayList<>(updatedEntityJson.size());
        // JsonNode node = mapper.valueToTree(updatedEntityJson);
        //  InvitroAssayInformation value = mapper.treeToValue(updatedEntityJson, InvitroAssayInformation.class);

            /*for(JsonNode n : updatedEntityJson){
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

        //return new ResponseEntity(savedAssay, HttpStatus.OK);
    }

    @PutGsrsRestApiMapping("/saveBulkAssays")
    //@Transactional
    public ResponseEntity<String> updateBulkAssay(@RequestBody JsonNode[] updatedEntityJson,
                                               @RequestParam Map<String, String> queryParameters,
                                               Principal principal) throws Exception {
        if (principal == null) {
            //not logged in!
          //  return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }


        List<InvitroAssayInformation> saveAssays = new ArrayList<>(updatedEntityJson.length);

        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < updatedEntityJson.length; i++) {
            //System.out.println("UPDATING ASSAY index " + i  + "    " + updatedEntityJson[i]);

            InvitroAssayInformation assayInfo = mapper.treeToValue(updatedEntityJson[i], InvitroAssayInformation.class);
           // assayInfos.add(assayInfo);

            InvitroAssayInformation savedAssay = invitroPharmacologyEntityService.saveBulkAssays(assayInfo);
            saveAssays.add(savedAssay);
        }

        return new ResponseEntity(saveAssays, HttpStatus.OK);

       // return gsrsControllerConfiguration.handleNotFound(queryParameters);

        //  List<T> l = new ArrayList<>(updatedEntityJson.size());
       // JsonNode node = mapper.valueToTree(updatedEntityJson);
      //  InvitroAssayInformation value = mapper.treeToValue(updatedEntityJson, InvitroAssayInformation.class);

            /*for(JsonNode n : updatedEntityJson){
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

        //return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutGsrsRestApiMapping("/saveBulkScreenings")
    //@Transactional
    public ResponseEntity<Object> updateBulkScreenings(@RequestBody JsonNode[] updatedEntityJson,
                                               @RequestParam Map<String, String> queryParameters,
                                               Principal principal) throws Exception {
        if (principal == null) {
            System.out.println("NOT LOGGED IN");
            //not logged in!
            return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }


        List<InvitroAssayScreening> assayInfos = new ArrayList<>(updatedEntityJson.length);

        List<InvitroAssayScreening> savedList = new ArrayList<InvitroAssayScreening>();
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < updatedEntityJson.length; i++) {

            // Convert Json to InvitroAssayScreening Class Object
            InvitroAssayScreening screeningObj = mapper.treeToValue(updatedEntityJson[i], InvitroAssayScreening.class);


           // screeningObj.setOwner();
            // Save/Update the InvitroAssayScreening record into the database
            /*
            InvitroAssayScreening savedScreeningObj = invitroPharmacologyEntityService.updateBulkScreenings(screeningObj, entityManager);
            */

          //  InvitroAssayScreening savedScreeningObj = invitroAssayScreeningController.updateBulkScreenings(screeningObj);

            InvitroAssayScreening savedScreeningObj = invitroPharmacologyEntityService.updateBulkScreenings(screeningObj, entityManager);

            if (savedScreeningObj != null) {
                savedList.add(savedScreeningObj);
            }
        }
      //  return new ResponseEntity(savedList, HttpStatus.OK);

        return gsrsControllerConfiguration.handleNotFound(queryParameters);
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
