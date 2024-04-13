package gov.hhs.gsrs.invitropharmacology.controllers;

import gov.hhs.gsrs.invitropharmacology.InvitroPharmacologyDataSourceConfig;
import gov.hhs.gsrs.invitropharmacology.SubstanceModuleService;
import gov.hhs.gsrs.invitropharmacology.models.*;
import gov.hhs.gsrs.invitropharmacology.LegacyAssayScreeningSearcher;
import gov.hhs.gsrs.invitropharmacology.services.*;

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
@ExposesResourceFor(InvitroAssayScreening.class)
@GsrsRestApiController(context = InvitroAssayScreeningEntityService.CONTEXT, idHelper = IdHelpers.NUMBER)
public class InvitroAssayScreeningController extends EtagLegacySearchEntityController<InvitroAssayScreeningController, InvitroAssayScreening, Long> {

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
    private InvitroAssayScreeningEntityService invitroAssayScreeningEntityService;

    @Autowired
    private SubstanceModuleService substanceModuleService;

    @Autowired
    private LegacyAssayScreeningSearcher legacyAssayScreeningSearcher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GsrsFactoryConfiguration gsrsFactoryConfiguration;


    @Override
    public GsrsEntityService<InvitroAssayScreening, Long> getEntityService() {
        return invitroAssayScreeningEntityService;
    }

    @Override
    protected LegacyGsrsSearchService<InvitroAssayScreening> getlegacyGsrsSearchService() {
        return legacyAssayScreeningSearcher;
    }

    @Override
    protected Stream<InvitroAssayScreening> filterStream(Stream<InvitroAssayScreening> stream, boolean publicOnly, Map<String, String> parameters) {
        return stream;
    }

    @GetGsrsRestApiMapping("/actuator/health")
    public ResponseEntity<Object> checkHealth() throws Exception {
        JSONObject status = new JSONObject();
        status.put("status", "UP");
        return new ResponseEntity(status, HttpStatus.OK);
    }

    @PutGsrsRestApiMapping("/saveBulkScreenings")
    //@Transactional
    public ResponseEntity<String> updateBulkScreenings(@RequestBody JsonNode[] updatedEntityJson,
                                               @RequestParam Map<String, String> queryParameters,
                                               Principal principal) throws Exception {
        if (principal == null) {
            System.out.println("NOT LOGGED IN");
            //not logged in!
           // return gsrsControllerConfiguration.unauthorized("no user logged in", queryParameters);
        }

        List<InvitroAssayScreening> assayInfos = new ArrayList<>(updatedEntityJson.length);
        System.out.println("SCREENING BULK SAVING *************************** ");

        List<InvitroAssayScreening> savedList = new ArrayList<InvitroAssayScreening>();
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < updatedEntityJson.length; i++) {
            System.out.println("**************** " + updatedEntityJson[i]);

            // Convert Json to InvitroAssayScreening Class Object
            InvitroAssayScreening screeningObj = mapper.treeToValue(updatedEntityJson[i], InvitroAssayScreening.class);

           // screeningObj.setOwner();
            // Save/Update the InvitroAssayScreening record into the database
            InvitroAssayScreening savedScreeningObj = invitroAssayScreeningEntityService.updateBulkScreenings(screeningObj, entityManager);

          //  InvitroAssayScreening savedScreeningObj = invitroPharmacologyEntityService.updateBulkScreenings(screeningObj, entityManager);

            if (savedScreeningObj != null) {
                System.out.println("SAVED THIS: " + savedScreeningObj);
                savedList.add(savedScreeningObj);
            }
        }
        return new ResponseEntity(savedList, HttpStatus.OK);

        //return gsrsControllerConfiguration.handleNotFound(queryParameters);
    }


    public InvitroAssayScreening updateBulkScreenings(InvitroAssayScreening screening) {
        try {
            // InvitroAssayScreening obj = null;
            //  entityManager.merge(screening);
            //  entityManager.persist(screening);
            //  entityManager.flush();

            InvitroAssayScreening obj = invitroAssayScreeningEntityService.updateBulkScreenings(screening, entityManager);
            System.out.println("******* SAVING " + obj);
            return obj;
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

}
