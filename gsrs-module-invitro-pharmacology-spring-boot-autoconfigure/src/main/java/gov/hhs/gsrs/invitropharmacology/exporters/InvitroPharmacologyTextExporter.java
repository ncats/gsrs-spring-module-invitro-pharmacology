package gov.hhs.gsrs.invitropharmacology.exporters;

import gov.hhs.gsrs.invitropharmacology.models.*;

import ix.ginas.exporters.*;

import java.io.IOException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

enum InvitroPharmTextDefaultColumns implements Column {
    SCREENING_NUMBER,
    ASSAY_SET,
    ID,
    ASSAY_ID,
    EXTERNAL_ASSAY_SOURCE,
    EXTERNAL_ASSAY_ID,
    EXTERNAL_ASSAY_REFERENCE_URL,
    ASSAY_TITLE,
    ASSAY_FORMAT,
    ASSAY_MODE,
    BIOASSAY_TYPE,
    BIOASSAY_CLASS,
    STUDY_TYPE,
    DETECTION_METHOD,
    PRESENTATION_TYPE,
    PRESENTATION,
    TARGET_SPECIES,
    TARGET_NAME,
    TARGET_NAME_APPROVAL_ID,
    HUMAN_HOMOLOG_TARGET,
    HUMAN_HOMOLOG_TARGET_APPROVAL_ID,
    LIGAND_SUBSTRATE,
    LIGAND_SUBSTRATE_APPROVAL_ID,
    LIGAND_SUBSTRATE_CONCENT,
    LIGAND_SUBSTRATE_CONCENT_UNITS,
    ANALYTES,
    REFERENCE_SOURCE_TYPE_AND_ID,
    LABORATORY_NAME,
    LABORATORY_CITY,
    SPONSOR_CONTACT_NAME,
    SPONSOR_REPORT_SUBMITTERS,
    REPORT_NUMBER,
    REPORT_DATE,
    BATCH_NUMBER,
    TEST_AGENT,
    TEST_AGENT_APPROVAL_ID,
    TEST_AGENT_CONCENTRATION,
    TEST_AGENT_CONCENTRATION_UNITS,
    RESULT_VALUE,
    RESULT_VALUE_UNITS,
    RESULT_TEST_DATE,
    CONTROL,
    CONTROL_TYPE,
    CONTROL_REFERENCE_VALUE,
    CONTROL_REFERENCE_VALUE_UNITS,
    CONTROL_RESULT_TYPE,
    SUMMARY_TARGET_NAME,
    SUMMARY_RESULT_VALUE_LOW,
    SUMMARY_RESULT_VALUE_AVERAGE,
    SUMMARY_RESULT_VALUE_HIGH,
    SUMMARY_RESULT_VALUE_UNITS,
    SUMMARY_RESULT_TYPE,
    SUMMARY_RELATIONSHIP_TYPE,
    SUMMARY_INTERACTION_TYPE,
    FROM_RESULT_DATA
}

@Slf4j
public class InvitroPharmacologyTextExporter implements Exporter<InvitroAssayInformation> {

    private final BufferedWriter bw;

    private int row = 1;
    private static int screeningNumber = 0;

    public InvitroPharmacologyTextExporter(OutputStream os, ExporterFactory.Parameters params) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(os));
        StringBuilder sb = new StringBuilder();

        sb.append("SCREENING_NUMBER").append("\t")
                .append("ASSAY_SET").append("\t")
                .append("ID").append("\t")
                .append("ASSAY_ID").append("\t")
                .append("EXTERNAL_ASSAY_SOURCE").append("\t")
                .append("EXTERNAL_ASSAY_ID").append("\t")
                .append("EXTERNAL_ASSAY_REFERENCE_URL").append("\t")
                .append("ASSAY_TITLE").append("\t")
                .append("ASSAY_FORMAT").append("\t")
                .append("AASSAY_MODE").append("\t")
                .append("BIOASSAY_TYPE").append("\t")
                .append("BIOASSAY_CLASS").append("\t")
                .append("STUDY_TYPE").append("\t")
                .append("DETECTION_METHOD").append("\t")
                .append("PRESENTATION_TYPE").append("\t")
                .append("PRESENTATION").append("\t")
                .append("TARGET_SPECIES").append("\t")
                .append("TARGET_NAME").append("\t")
                .append("TARGET_NAME_APPROVAL_ID").append("\t")
                .append("HUMAN_HOMOLOG_TARGET").append("\t")
                .append("HUMAN_HOMOLOG_TARGET_APPROVAL_ID").append("\t")
                .append("LIGAND_SUBSTRATE").append("\t")
                .append("LIGAND_SUBSTRATE_APPROVAL_ID").append("\t")
                .append("LIGAND_SUBSTRATE_CONCENT").append("\t")
                .append("LIGAND_SUBSTRATE_CONCENT_UNITS").append("\t")
                .append("ANALYTES").append("\t")
                .append("REFERENCE_SOURCE_TYPE_AND_ID").append("\t")
                .append("LABORATORY_NAME").append("\t")
                .append("LABORATORY_CITY").append("\t")
                .append("SPONSOR_CONTACT_NAME").append("\t")
                .append("SPONSOR_REPORT_SUBMITTERS").append("\t")
                .append("REPORT_NUMBER").append("\t")
                .append("REPORT_DATE").append("\t")
                .append("BATCH_NUMBER").append("\t")
                .append("TEST_AGENT").append("\t")
                .append("TEST_AGENT_APPROVAL_ID").append("\t")
                .append("TEST_AGENT_CONCENTRATION").append("\t")
                .append("TEST_AGENT_CONCENTRATION_UNITS").append("\t")
                .append("RESULT_VALUE").append("\t")
                .append("RESULT_VALUE_UNITS").append("\t")
                .append("RESULT_TEST_DATE").append("\t")
                .append("CONTROL").append("\t")
                .append("CONTROL_TYPE").append("\t")
                .append("CONTROL_REFERENCE_VALUE").append("\t")
                .append("CONTROL_REFERENCE_VALUE_UNITS").append("\t")
                .append("CONTROL_RESULT_TYPE").append("\t")
                .append("SUMMARY_TARGET_NAME").append("\t")
                .append("SUMMARY_RESULT_VALUE_LOW").append("\t")
                .append("SUMMARY_RESULT_VALUE_AVERAGE").append("\t")
                .append("SUMMARY_RESULT_VALUE_HIGH").append("\t")
                .append("SUMMARY_RESULT_VALUE_UNITS").append("\t")
                .append("SUMMARY_RESULT_TYPE").append("\t")
                .append("SUMMARY_RELATIONSHIP_TYPE").append("\t")
                .append("SUMMARY_INTERACTION_TYPE").append("\t")
                .append("FROM_RESULT_DATA").append("\t");

        bw.write(sb.toString());
        bw.newLine();
    }

    @Override
    public void export(InvitroAssayInformation a) throws IOException {
        /*****************************************************************************/
        // Export Assay records and also display all the screening data in each row
        /****************************************************************************/
        try {
            // Add one more column called "Screening Number" at the beginning.  Have it increment by one.
            // Each of these screenings be new rows. Can duplicate the other Assay columns on each row.

            // If there is no screening data, only export the Assay details
            if (a.invitroAssayScreenings.size() == 0) {
                publicCreateRows(a, 0);
                // Else if there are screening data, export screening data along with Assay details
            } else if (a.invitroAssayScreenings.size() > 0) {
                for (int i = 0; i < a.invitroAssayScreenings.size(); i++) {
                    publicCreateRows(a, i);
                } // for InvitroAssayScreenings
            } // invitroAssayScreenings size > 0
        } // try
        catch (Exception ex) {
            log.error("Error exporting In-vitro Pharmacology for ID: " + a.id, ex);
        }
    }

    public void publicCreateRows(InvitroAssayInformation a, int i) {
        int col = 0;
        this.screeningNumber = i;

        int screeningNum = screeningNumber + 1;

        try {
            StringBuilder sb = new StringBuilder();

            sb.append(screeningNum).append("\t");  // SCREENING_NUMBER

            StringBuilder sbAssaySet = getAssaySetDetails(a, InvitroPharmTextDefaultColumns.ASSAY_SET);
            sb.append(sbAssaySet.toString()).append("\t");  // ASSAY_SET

            sb.append((a.id.toString() != null) ? a.id.toString() : "").append("\t");  // ID

            sb.append((a.assayId != null) ? a.assayId : "").append("\t");  // ASSAY_ID

            sb.append((a.externalAssaySource != null) ? a.externalAssaySource : "").append("\t");  // EXTERNAL_ASSAY_SOURCE

            sb.append((a.externalAssayId != null) ? a.externalAssayId : "").append("\t");  // EXTERNAL_ASSAY_ID

            sb.append((a.externalAssayReferenceUrl != null) ? a.externalAssayReferenceUrl : "").append("\t");  // EXTERNAL_ASSAY_REFERENCE_URL

            sb.append((a.assayTitle != null) ? a.assayTitle : "").append("\t");  // ASSAY_TITLE

            sb.append((a.assayFormat != null) ? a.assayFormat : "").append("\t");  // ASSAY_FORMAT

            sb.append((a.assayMode != null) ? a.assayMode : "").append("\t");  // ASSAY_MODE

            sb.append((a.bioassayType != null) ? a.bioassayType : "").append("\t");  // BIOASSAY_TYPE

            sb.append((a.bioassayClass != null) ? a.bioassayClass : "").append("\t");  // BIOASSAY_CLASS

            sb.append((a.studyType != null) ? a.studyType : "").append("\t");  // STUDY_TYPE

            sb.append((a.detectionMethod != null) ? a.detectionMethod : "").append("\t");  // DETECTION_METHOD

            sb.append((a.presentationType != null) ? a.presentationType : "").append("\t");  // PRESENTATION_TYPE

            sb.append((a.presentation != null) ? a.presentation : "").append("\t");  // PRESENTATION

            sb.append((a.targetSpecies != null) ? a.targetSpecies : "").append("\t");  // TARGET_SPECIES

            sb.append((a.targetName != null) ? a.targetName : "").append("\t");  // TARGET_NAME

            sb.append((a.targetNameApprovalId != null) ? a.targetNameApprovalId : "").append("\t");  // TARGET_NAME_APPROVAL_ID

            sb.append((a.humanHomologTarget != null) ? a.humanHomologTarget : "").append("\t");  // HUMAN_HOMOLOG_TARGET

            sb.append((a.humanHomologTargetApprovalId != null) ? a.humanHomologTargetApprovalId : "").append("\t");  // HUMAN_HOMOLOG_TARGET_APPROVAL_ID

            sb.append((a.ligandSubstrate != null) ? a.ligandSubstrate : "").append("\t");  // LIGAND_SUBSTRATE

            sb.append((a.ligandSubstrateApprovalId != null) ? a.ligandSubstrateApprovalId : "").append("\t");  // LIGAND_SUBSTRATE_APPROVAL_ID

            sb.append((a.standardLigandSubstrateConcentration != null) ? a.standardLigandSubstrateConcentration : "").append("\t");  // LIGAND_SUBSTRATE_CONCENT

            sb.append((a.standardLigandSubstrateConcentrationUnits != null) ? a.standardLigandSubstrateConcentrationUnits : "").append("\t");  // LIGAND_SUBSTRATE_CONCENT_UNITS

            StringBuilder sbAnalytes = getAssayAnalytesDetails(a, InvitroPharmTextDefaultColumns.ANALYTES);
            sb.append(sbAnalytes.toString()).append("\t");  // ANALYTES

            // SCREENING DATA
            StringBuilder sbReference = getScreeningDetails(a, InvitroPharmTextDefaultColumns.REFERENCE_SOURCE_TYPE_AND_ID);
            sb.append(sbReference.toString()).append("\t");  // REFERENCE_SOURCE_TYPE_AND_ID

            StringBuilder sbLaboratory = getScreeningDetails(a, InvitroPharmTextDefaultColumns.LABORATORY_NAME);
            sb.append(sbLaboratory.toString()).append("\t");  // LABORATORY_NAME

            StringBuilder sbLaboratoryCity = getScreeningDetails(a, InvitroPharmTextDefaultColumns.LABORATORY_CITY);
            sb.append(sbLaboratoryCity.toString()).append("\t");  // LABORATORY_CITY

            StringBuilder sbSponsorName = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SPONSOR_CONTACT_NAME);
            sb.append(sbSponsorName.toString()).append("\t");  // SPONSOR_CONTACT_NAME

            StringBuilder sbSubmitters = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SPONSOR_REPORT_SUBMITTERS);
            sb.append(sbSubmitters.toString()).append("\t");  // SPONSOR_REPORT_SUBMITTERS

            StringBuilder sbReportNumber = getScreeningDetails(a, InvitroPharmTextDefaultColumns.REPORT_NUMBER);
            sb.append(sbReportNumber.toString()).append("\t");  // REPORT_NUMBER

            StringBuilder sbReportDate = getScreeningDetails(a, InvitroPharmTextDefaultColumns.REPORT_DATE);
            sb.append(sbReportDate.toString()).append("\t");  // REPORT_DATE

            StringBuilder sbBatchNumber = getScreeningDetails(a, InvitroPharmTextDefaultColumns.BATCH_NUMBER);
            sb.append(sbBatchNumber.toString()).append("\t");  // BATCH_NUMBER

            StringBuilder sbTestAgent = getScreeningDetails(a, InvitroPharmTextDefaultColumns.TEST_AGENT);
            sb.append(sbTestAgent.toString()).append("\t");  // TEST_AGENT

            StringBuilder sbTestAgentApprovalId = getScreeningDetails(a, InvitroPharmTextDefaultColumns.TEST_AGENT_APPROVAL_ID);
            sb.append(sbTestAgentApprovalId.toString()).append("\t");  // TEST_AGENT_APPROVAL_ID

            StringBuilder sbTestAgentConcent = getScreeningDetails(a, InvitroPharmTextDefaultColumns.TEST_AGENT_CONCENTRATION);
            sb.append(sbTestAgentConcent.toString()).append("\t");  // TEST_AGENT_CONCENTRATION

            StringBuilder sbTestAgentConcentUnits = getScreeningDetails(a, InvitroPharmTextDefaultColumns.TEST_AGENT_CONCENTRATION_UNITS);
            sb.append(sbTestAgentConcentUnits.toString()).append("\t");  // TEST_AGENT_CONCENTRATION_UNITS

            StringBuilder sbResultValue = getScreeningDetails(a, InvitroPharmTextDefaultColumns.RESULT_VALUE);
            sb.append(sbResultValue.toString()).append("\t");  // RESULT_VALUE

            StringBuilder sbResultValueUnits = getScreeningDetails(a, InvitroPharmTextDefaultColumns.RESULT_VALUE_UNITS);
            sb.append(sbResultValueUnits.toString()).append("\t");  // RESULT_VALUE_UNITS

            StringBuilder sbResultTestDate = getScreeningDetails(a, InvitroPharmTextDefaultColumns.RESULT_TEST_DATE);
            sb.append(sbResultTestDate.toString()).append("\t");  // RESULT_TEST_DATE

            StringBuilder sbControl = getControlDetails(a, InvitroPharmTextDefaultColumns.CONTROL);
            sb.append(sbControl.toString()).append("\t");  // CONTROL

            StringBuilder sbControlType = getControlDetails(a, InvitroPharmTextDefaultColumns.CONTROL_TYPE);
            sb.append(sbControlType.toString()).append("\t");  // CONTROL_TYPE

            StringBuilder sbControlRefValue = getControlDetails(a, InvitroPharmTextDefaultColumns.CONTROL_REFERENCE_VALUE);
            sb.append(sbControlRefValue.toString()).append("\t");  // CONTROL_REFERENCE_VALUE

            StringBuilder sbControlRefValueUnits = getControlDetails(a, InvitroPharmTextDefaultColumns.CONTROL_REFERENCE_VALUE_UNITS);
            sb.append(sbControlRefValueUnits.toString()).append("\t");  // CONTROL_REFERENCE_VALUE_UNITS

            StringBuilder sbControlResultType = getControlDetails(a, InvitroPharmTextDefaultColumns.CONTROL_RESULT_TYPE);
            sb.append(sbControlResultType.toString()).append("\t");  // CONTROL_RESULT_TYPE

            StringBuilder sbSummaryTargetName = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_TARGET_NAME);
            sb.append(sbSummaryTargetName.toString()).append("\t");  // SUMMARY_TARGET_NAME

            StringBuilder sbSummaryResultValueLow = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RESULT_VALUE_LOW);
            sb.append(sbSummaryResultValueLow.toString()).append("\t");  // SUMMARY_RESULT_VALUE_LOW

            StringBuilder sbSummaryResultValueAvg = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RESULT_VALUE_AVERAGE);
            sb.append(sbSummaryResultValueAvg.toString()).append("\t");  // SUMMARY_RESULT_VALUE_AVERAGE

            StringBuilder sbSummaryResultValueHigh = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RESULT_VALUE_HIGH);
            sb.append(sbSummaryResultValueHigh.toString()).append("\t");  // SUMMARY_RESULT_VALUE_HIGH

            StringBuilder sbSummaryResultValueUnits = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RESULT_VALUE_UNITS);
            sb.append(sbSummaryResultValueUnits.toString()).append("\t");  // SUMMARY_RESULT_VALUE_UNITS

            StringBuilder sbSummaryResultType = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RESULT_TYPE);
            sb.append(sbSummaryResultType.toString()).append("\t");  // SUMMARY_RESULT_TYPE

            StringBuilder sbSummaryRelType = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_RELATIONSHIP_TYPE);
            sb.append(sbSummaryRelType.toString()).append("\t");  // SUMMARY_RELATIONSHIP_TYPE

            StringBuilder sbSummaryInteractType = getScreeningDetails(a, InvitroPharmTextDefaultColumns.SUMMARY_INTERACTION_TYPE);
            sb.append(sbSummaryInteractType.toString()).append("\t");  // SUMMARY_INTERACTION_TYPE

            StringBuilder sbFromResultData = getScreeningDetails(a, InvitroPharmTextDefaultColumns.FROM_RESULT_DATA);
            sb.append(sbFromResultData.toString()).append("\t");  // FROM_RESULT_DATA

            // Write to buffer
            this.bw.write(sb.toString());
            this.bw.newLine();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }

    private static StringBuilder getAssayAnalytesDetails(InvitroAssayInformation a, InvitroPharmTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (a.invitroAssayAnalytes.size() > 0) {

            for (int i = 0; i < a.invitroAssayAnalytes.size(); i++) {
                InvitroAssayAnalyte analy = a.invitroAssayAnalytes.get(i);

                if (i > 0) {
                    sb.append("|");
                }

                if (analy != null) {
                    sb.append(analy.analyte != null ? analy.analyte : "");
                }
            } // for invitroAssayAnalytes
        } // if invitroAssayAnalytes.size() > 0

        return sb;
    }

    private static StringBuilder getAssaySetDetails(InvitroAssayInformation a, InvitroPharmTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (a.invitroAssaySets.size() > 0) {

            for (int i = 0; i < a.invitroAssaySets.size(); i++) {
                InvitroAssaySet asySet = a.invitroAssaySets.get(i);

                if (i > 0) {
                    sb.append("|");
                }

                if (asySet != null) {
                    sb.append(asySet.assaySet != null ? asySet.assaySet : "");
                }

            } // for invitroAssaySets
        } // if invitroAssaySets.size() > 0

        return sb;
    }

    private static StringBuilder getControlDetails(InvitroAssayInformation a, InvitroPharmTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (a.invitroAssayScreenings.size() > 0) {

            InvitroAssayScreening screening = a.invitroAssayScreenings.get(screeningNumber);

            if (screening.invitroControls.size() > 0) {

                for (int i = 0; i < screening.invitroControls.size(); i++) {
                    InvitroControl ctrl = screening.invitroControls.get(i);

                    if (i > 0) {
                        sb.append("|");
                    }

                    if (ctrl != null) {
                        switch (fieldName) {
                            case CONTROL:
                                sb.append(ctrl.control != null ? ctrl.control : "");
                                break;
                            case CONTROL_TYPE:
                                sb.append(ctrl.controlType != null ? ctrl.controlType : "");
                                break;
                            case CONTROL_REFERENCE_VALUE:
                                sb.append(ctrl.controlReferenceValue != null ? ctrl.controlReferenceValue : "");
                                break;
                            case CONTROL_REFERENCE_VALUE_UNITS:
                                sb.append(ctrl.controlReferenceValueUnits != null ? ctrl.controlReferenceValueUnits : "");
                                break;
                            case CONTROL_RESULT_TYPE:
                                sb.append(ctrl.controlResultType != null ? ctrl.controlResultType : "");
                                break;
                            default:
                                break;
                        } // switch
                    } // if control object exists

                } // for invitroControls

            } // if invitroControls.size() > 0
        } // invitroAssayScreenings.size() > 0

        return sb;
    }

    private static StringBuilder getScreeningDetails(InvitroAssayInformation a, InvitroPharmTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (a.invitroAssayScreenings.size() > 0) {

            InvitroAssayScreening screening = a.invitroAssayScreenings.get(screeningNumber);

            if (screening.invitroAssayResultInformation != null) {

                switch (fieldName) {

                    case REFERENCE_SOURCE_TYPE_AND_ID:

                        if (screening.invitroAssayResultInformation.invitroReferences.size() > 0) {
                            for (int i = 0; i < screening.invitroAssayResultInformation.invitroReferences.size(); i++) {
                                InvitroReference reference = screening.invitroAssayResultInformation.invitroReferences.get(i);

                                if (i > 0) {
                                    sb.append("|");
                                }
                                if (reference != null) {
                                    sb.append((reference.sourceType != null) ? reference.sourceType : "");

                                    // Append Source Id to Source Type
                                    sb.append((reference.sourceId != null) ? " " + reference.sourceId : "");
                                } // if reference is not null
                            } // for
                        }  // if references size > 0
                        break;
                    case LABORATORY_NAME:
                        sb.append((screening.invitroAssayResultInformation.invitroLaboratory != null && screening.invitroAssayResultInformation.invitroLaboratory.laboratoryName != null)
                                ? screening.invitroAssayResultInformation.invitroLaboratory.laboratoryName : "");
                        break;
                    case LABORATORY_CITY:
                        sb.append((screening.invitroAssayResultInformation.invitroLaboratory != null && screening.invitroAssayResultInformation.invitroLaboratory.laboratoryCity != null)
                                ? screening.invitroAssayResultInformation.invitroLaboratory.laboratoryCity : "");
                        break;
                    case SPONSOR_CONTACT_NAME:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsor != null && screening.invitroAssayResultInformation.invitroSponsor.sponsorContactName != null)
                                ? screening.invitroAssayResultInformation.invitroSponsor.sponsorContactName : "");
                        break;
                    case SPONSOR_REPORT_SUBMITTERS:
                        if (screening.invitroAssayResultInformation.invitroSponsorReport.invitroSponsorSubmitters.size() > 0) {
                            for (int i = 0; i < screening.invitroAssayResultInformation.invitroSponsorReport.invitroSponsorSubmitters.size(); i++) {
                                InvitroSponsorSubmitter submitter = screening.invitroAssayResultInformation.invitroSponsorReport.invitroSponsorSubmitters.get(i);

                                if (i > 0) {
                                    sb.append("|");
                                }

                                if (submitter != null) {
                                    sb.append(submitter.sponsorReportSubmitterName != null ? submitter.sponsorReportSubmitterName : "");
                                }
                            } // for invitroSponsorSubmitters
                        } // if invitroSponsorSubmitters.size() > 0
                        break;
                    case REPORT_NUMBER:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsorReport != null && screening.invitroAssayResultInformation.invitroSponsorReport.reportNumber != null)
                                ? screening.invitroAssayResultInformation.invitroSponsorReport.reportNumber : "");
                        break;
                    case REPORT_DATE:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsorReport != null && screening.invitroAssayResultInformation.invitroSponsorReport.reportDate != null)
                                ? a.convertDateToString(screening.invitroAssayResultInformation.invitroSponsorReport.reportDate) : "");
                        break;
                    case BATCH_NUMBER:
                        sb.append((screening.invitroAssayResultInformation.batchNumber != null)
                                ? screening.invitroAssayResultInformation.batchNumber : "");
                        break;
                    case TEST_AGENT:
                        sb.append((screening.invitroAssayResultInformation.invitroTestAgent != null && screening.invitroAssayResultInformation.invitroTestAgent.testAgent != null)
                                ? screening.invitroAssayResultInformation.invitroTestAgent.testAgent : "");
                        break;
                    case TEST_AGENT_APPROVAL_ID:
                        sb.append((screening.invitroAssayResultInformation.invitroTestAgent != null && screening.invitroAssayResultInformation.invitroTestAgent.testAgentApprovalId != null)
                                ? screening.invitroAssayResultInformation.invitroTestAgent.testAgentApprovalId : "");
                        break;
                    case TEST_AGENT_CONCENTRATION:
                        sb.append((screening.invitroAssayResult != null && screening.invitroAssayResult.testAgentConcentration != null)
                                ? screening.invitroAssayResult.testAgentConcentration : "");
                        break;
                    case TEST_AGENT_CONCENTRATION_UNITS:
                        sb.append((screening.invitroAssayResult != null && screening.invitroAssayResult.testAgentConcentrationUnits != null)
                                ? screening.invitroAssayResult.testAgentConcentrationUnits : "");
                        break;
                    case RESULT_VALUE:
                        sb.append((screening.invitroAssayResult != null && screening.invitroAssayResult.resultValue != null)
                                ? screening.invitroAssayResult.resultValue : "");
                        break;
                    case RESULT_VALUE_UNITS:
                        sb.append((screening.invitroAssayResult != null && screening.invitroAssayResult.resultValueUnits != null)
                                ? screening.invitroAssayResult.resultValueUnits : "");
                        break;
                    case RESULT_TEST_DATE:
                        sb.append((screening.invitroAssayResult != null && screening.invitroAssayResult.testDate != null)
                                ? a.convertDateToString(screening.invitroAssayResult.testDate) : "");
                        break;
                    case SUMMARY_TARGET_NAME:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.targetName != null)
                                ? screening.invitroSummary.targetName : "");
                        break;
                    case SUMMARY_RESULT_VALUE_LOW:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.resultValueLow != null)
                                ? screening.invitroSummary.resultValueLow : "");
                        break;
                    case SUMMARY_RESULT_VALUE_AVERAGE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.resultValueAverage != null)
                                ? screening.invitroSummary.resultValueAverage : "");
                        break;
                    case SUMMARY_RESULT_VALUE_HIGH:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.resultValueHigh != null)
                                ? screening.invitroSummary.resultValueHigh : "");
                        break;
                    case SUMMARY_RESULT_VALUE_UNITS:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.resultValueUnits != null)
                                ? screening.invitroSummary.resultValueUnits : "");
                        break;
                    case SUMMARY_RESULT_TYPE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.resultType != null)
                                ? screening.invitroSummary.resultType : "");
                        break;
                    case SUMMARY_RELATIONSHIP_TYPE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.relationshipType != null)
                                ? screening.invitroSummary.relationshipType : "");
                        break;
                    case SUMMARY_INTERACTION_TYPE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.interactionType != null)
                                ? screening.invitroSummary.interactionType : "");
                        break;
                    case FROM_RESULT_DATA:
                        String isFromResult = "";
                        if (screening.invitroSummary != null && screening.invitroSummary.isFromResult != null) {
                            if (screening.invitroSummary.isFromResult == true) {
                                isFromResult = "Yes";
                            } else if (screening.invitroSummary.isFromResult == false) {
                                isFromResult = "No";
                            } else {
                                // Do something
                            }
                        }
                        sb.append(isFromResult);
                        break;
                    default:
                        break;
                }
            }

        } // if invitroAssayResultInformation exists

        return sb;
    }

}