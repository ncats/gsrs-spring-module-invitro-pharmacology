package gov.hhs.gsrs.invitropharmacology.exporters;

import gov.hhs.gsrs.invitropharmacology.models.*;

import ix.ginas.exporters.*;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

enum InvitroPharmDefaultColumns implements Column {
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
    TARGET_NAME,
    TARGET_NAME_APPROVAL_ID,
    TARGET_SPECIES,
    HUMAN_HOMOLOG_TARGET,
    HUMAN_HOMOLOG_TARGET_APPROVAL_ID,
    LIGAND_SUBSTRATE,
    LIGAND_SUBSTRATE_APPROVAL_ID,
    LIGAND_SUBSTRATE_CONCENT,
    LIGAND_SUBSTRATE_CONCENT_UNITS,
    REFERENCE_SOURCE_TYPE_AND_ID,
    LABORATORY_NAME,
    SPONSOR_CONTACT_NAME,
    REPORT_NUMBER,
    REPORT_DATE,
    BATCH_NUMBER,
    TEST_AGENT,
    TEST_AGENT_APPROVAL_ID,
    TEST_AGENT_CONCENTRATION,
    TEST_AGENT_CONCENTRATION_UNITS,
    RESULT_VALUE,
    RESULT_VALUE_UNITS,
    SUMMARY_RELATIONSHIP,
    SUMMARY_INTERACTION_TYPE,
    SUMMARY_AVERAGE,
    SUMMARY_LOW,
    SUMMARY_HIGH,
    SUMMARY_UNITS
}

@Slf4j
public class InvitroPharmacologyExporter implements Exporter<InvitroAssayInformation> {

    private final Spreadsheet spreadsheet;

    private int row = 1;
    private static int screeningNumber = 0;

    private final List<ColumnValueRecipe<InvitroAssayInformation>> recipeMap;

    private InvitroPharmacologyExporter(Builder builder) {
        this.spreadsheet = builder.spreadsheet;
        this.recipeMap = builder.columns;

        int j = 0;
        Spreadsheet.SpreadsheetRow header = spreadsheet.getRow(0);
        for (ColumnValueRecipe<InvitroAssayInformation> col : recipeMap) {
            j += col.writeHeaderValues(header, j);
        }
    }

    @Override
    public void export(InvitroAssayInformation a) throws IOException {
        /***************************************************************************************/
        // Export In-vitro Pharmacology records and also display all the screenings in each row
        /***************************************************************************************/
        try {
            // Add one more column called "Screening Number" at the beginning.  Have it increment by one.
            // Each of these screenings be new rows. Can duplicate the other Assay columns on each row.

            // If there is no screening data, only export the Assay details
            if (a.invitroAssayScreenings.size() == 0) {
                createRows(a, 0);
                // Else if there are screening data, export screening data along with Assay details
            } else if (a.invitroAssayScreenings.size() > 0) {
                for (int i = 0; i < a.invitroAssayScreenings.size(); i++) {
                    createRows(a, i);
                } // for InvitroAssayScreenings
            } // invitroAssayScreenings size > 0

        } // try
        catch (Exception ex) {
            log.error("Error exporting In-vitro Pharmacology for ID: " + a.id, ex);
        }
    }

    public void createRows(InvitroAssayInformation a, int i) {
        Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);
        int col = 0;
        this.screeningNumber = i;

        for (ColumnValueRecipe<InvitroAssayInformation> recipe : recipeMap) {
            col += recipe.writeValuesFor(row, col, a);
        }
    }

    @Override
    public void close() throws IOException {
        spreadsheet.close();
    }

    private static Map<Column, ColumnValueRecipe<InvitroAssayInformation>> DEFAULT_RECIPE_MAP;

    static {

        DEFAULT_RECIPE_MAP = new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.SCREENING_NUMBER, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.SCREENING_NUMBER, (a, cell) -> {
            int screeningNum = screeningNumber;
            // If there is any screening data, increment the number by 1
            if (a.invitroAssayScreenings.size() > 0) {
                screeningNum = screeningNumber + 1;
            }
            cell.writeInteger((screeningNum));
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ASSAY_SET, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ASSAY_SET, (s, cell) -> {
            StringBuilder sb = getAssaySetDetails(s, InvitroPharmDefaultColumns.ASSAY_SET);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ID, (a, cell) -> cell.writeString(
                a.id.toString()
        )));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ASSAY_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ASSAY_ID, (a, cell) -> cell.writeString(a.assayId)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_SOURCE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_SOURCE, (a, cell) -> cell.writeString(a.externalAssaySource)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_ID, (a, cell) -> cell.writeString(a.externalAssayId)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_REFERENCE_URL, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.EXTERNAL_ASSAY_REFERENCE_URL, (a, cell) -> cell.writeString(a.externalAssayReferenceUrl)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ASSAY_TITLE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ASSAY_TITLE, (a, cell) -> cell.writeString(a.assayTitle)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ASSAY_FORMAT, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ASSAY_FORMAT, (a, cell) -> cell.writeString(a.assayFormat)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.ASSAY_MODE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.ASSAY_MODE, (a, cell) -> cell.writeString(a.assayMode)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.BIOASSAY_TYPE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.BIOASSAY_TYPE, (a, cell) -> cell.writeString(a.bioassayType)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.BIOASSAY_CLASS, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.BIOASSAY_CLASS, (a, cell) -> cell.writeString(a.bioassayClass)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.STUDY_TYPE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.STUDY_TYPE, (a, cell) -> cell.writeString(a.studyType)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.DETECTION_METHOD, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.DETECTION_METHOD, (a, cell) -> cell.writeString(a.detectionMethod)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.PRESENTATION_TYPE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.PRESENTATION_TYPE, (a, cell) -> cell.writeString(a.presentationType)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.PRESENTATION, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.PRESENTATION, (a, cell) -> cell.writeString(a.presentation)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TARGET_NAME, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TARGET_NAME, (a, cell) -> cell.writeString(a.targetName)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TARGET_NAME_APPROVAL_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TARGET_NAME_APPROVAL_ID, (a, cell) -> cell.writeString(a.targetNameApprovalId)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TARGET_SPECIES, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TARGET_SPECIES, (a, cell) -> cell.writeString(a.targetSpecies)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.HUMAN_HOMOLOG_TARGET, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.HUMAN_HOMOLOG_TARGET, (a, cell) -> cell.writeString(a.humanHomologTarget)));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.HUMAN_HOMOLOG_TARGET_APPROVAL_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.HUMAN_HOMOLOG_TARGET_APPROVAL_ID, (a, cell) -> cell.writeString(a.humanHomologTargetApprovalId)));

        // SCREENING DATA
        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.REFERENCE_SOURCE_TYPE_AND_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.REFERENCE_SOURCE_TYPE_AND_ID, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.REFERENCE_SOURCE_TYPE_AND_ID);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.LABORATORY_NAME, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.LABORATORY_NAME, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.LABORATORY_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.SPONSOR_CONTACT_NAME, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.SPONSOR_CONTACT_NAME, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.SPONSOR_CONTACT_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.REPORT_NUMBER, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.REPORT_NUMBER, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.REPORT_NUMBER);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.REPORT_DATE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.REPORT_DATE, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.REPORT_DATE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TEST_AGENT, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TEST_AGENT, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.TEST_AGENT);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TEST_AGENT_APPROVAL_ID, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TEST_AGENT_APPROVAL_ID, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.TEST_AGENT_APPROVAL_ID);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION_UNITS, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION_UNITS, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.TEST_AGENT_CONCENTRATION_UNITS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.RESULT_VALUE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.RESULT_VALUE, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.RESULT_VALUE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.RESULT_VALUE_UNITS, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.RESULT_VALUE_UNITS, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.RESULT_VALUE_UNITS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.SUMMARY_RELATIONSHIP, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.SUMMARY_RELATIONSHIP, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.SUMMARY_RELATIONSHIP);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(InvitroPharmDefaultColumns.SUMMARY_INTERACTION_TYPE, SingleColumnValueRecipe.create(InvitroPharmDefaultColumns.SUMMARY_INTERACTION_TYPE, (s, cell) -> {
            StringBuilder sb = getScreeningDetails(s, InvitroPharmDefaultColumns.SUMMARY_INTERACTION_TYPE);
            cell.writeString(sb.toString());
        }));

    }  // static

    private static StringBuilder getAssaySetDetails(InvitroAssayInformation a, InvitroPharmDefaultColumns fieldName) {
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

    private static StringBuilder getScreeningDetails(InvitroAssayInformation a, InvitroPharmDefaultColumns fieldName) {
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
                    case SPONSOR_CONTACT_NAME:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsor != null && screening.invitroAssayResultInformation.invitroSponsor.sponsorContactName != null)
                                ? screening.invitroAssayResultInformation.invitroSponsor.sponsorContactName : "");
                        break;
                    case REPORT_NUMBER:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsorReport != null && screening.invitroAssayResultInformation.invitroSponsorReport.reportNumber != null)
                                ? screening.invitroAssayResultInformation.invitroSponsorReport.reportNumber : "");
                        break;
                    case REPORT_DATE:
                        sb.append((screening.invitroAssayResultInformation.invitroSponsorReport != null && screening.invitroAssayResultInformation.invitroSponsorReport.reportDate != null)
                                ? screening.invitroAssayResultInformation.invitroSponsorReport.reportDate : "");
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
                    case SUMMARY_RELATIONSHIP:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.relationshipType != null)
                                ? screening.invitroSummary.relationshipType : "");
                        break;
                    case SUMMARY_INTERACTION_TYPE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.interactionType != null)
                                ? screening.invitroSummary.interactionType : "");
                        break;
                    case SUMMARY_AVERAGE:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.interactionType != null)
                                ? screening.invitroSummary.interactionType : "");
                        break;
                    case SUMMARY_LOW:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.interactionType != null)
                                ? screening.invitroSummary.interactionType : "");
                        break;
                    case SUMMARY_HIGH:
                        sb.append((screening.invitroSummary != null && screening.invitroSummary.interactionType != null)
                                ? screening.invitroSummary.interactionType : "");
                        break;
                    default:
                        break;
                }
            }

        } // if invitroAssayResultInformation exists

        return sb;
    }

    /**
     * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
     * but these may be modified using the add/remove column methods.
     */
    public static class Builder {
        private final List<ColumnValueRecipe<InvitroAssayInformation>> columns = new ArrayList<>();
        private final Spreadsheet spreadsheet;

        private boolean publicOnly = false;

        /**
         * Create a new Builder that uses the given Spreadsheet to write to.
         *
         * @param spreadSheet the {@link Spreadsheet} object that will be written to by this exporter. can not be null.
         * @throws NullPointerException if spreadsheet is null.
         */
        public Builder(Spreadsheet spreadSheet) {
            Objects.requireNonNull(spreadSheet);
            this.spreadsheet = spreadSheet;

            for (Map.Entry<Column, ColumnValueRecipe<InvitroAssayInformation>> entry : DEFAULT_RECIPE_MAP.entrySet()) {
                columns.add(entry.getValue());
            }
        }

        public Builder addColumn(Column column, ColumnValueRecipe<InvitroAssayInformation> recipe) {
            return addColumn(column.name(), recipe);
        }

        public Builder addColumn(String columnName, ColumnValueRecipe<InvitroAssayInformation> recipe) {
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(recipe);
            columns.add(recipe);

            return this;
        }

        public Builder renameColumn(Column oldColumn, String newName) {
            return renameColumn(oldColumn.name(), newName);
        }

        public Builder renameColumn(String oldName, String newName) {
            //use iterator to preserve order
            ListIterator<ColumnValueRecipe<InvitroAssayInformation>> iter = columns.listIterator();
            while (iter.hasNext()) {

                ColumnValueRecipe<InvitroAssayInformation> oldValue = iter.next();
                ColumnValueRecipe<InvitroAssayInformation> newValue = oldValue.replaceColumnName(oldName, newName);
                if (oldValue != newValue) {
                    iter.set(newValue);
                }
            }
            return this;
        }

        public InvitroPharmacologyExporter build() {
            return new InvitroPharmacologyExporter(this);
        }

        public Builder includePublicDataOnly(boolean publicOnly) {
            this.publicOnly = publicOnly;
            return this;
        }

    }
}