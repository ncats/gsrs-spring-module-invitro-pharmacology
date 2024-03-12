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

@Slf4j
public class InvitroPharmacologyTextExporter implements Exporter<InvitroAssayInformation> {

    private final BufferedWriter bw;

    private int row = 1;
    private static int screeningNumber = 0;

    public InvitroPharmacologyTextExporter(OutputStream os, ExporterFactory.Parameters params) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(os));
        StringBuilder sb = new StringBuilder();

        sb.append("SCREENING_NUMBER").append("\t")
                .append("ID").append("\t")
                .append("ASSAY_ID").append("\t")
                .append("EXTERNAL_ASSAY_ID").append("\t")
                .append("EXTERNAL_ASSAY_SOURCE").append("\t")
                .append("EXTERNAL_ASSAY_REFERENCE").append("\t")
                .append("EXTERNAL_ASSAY_REFERENCE_URL").append("\t")
                .append("ASSAY_TITLE").append("\t")
                .append("ASSAY_FORMAT").append("\t")
                .append("BIOASSAY_TYPE").append("\t")
                .append("BIOASSAY_CLASS").append("\t")
                .append("STUDY_TYPE").append("\t")
                .append("DETECTION_METHOD").append("\t")
                .append("PRESENTATION_TYPE").append("\t")
                .append("PRESENTATION").append("\t")
                .append("BUFFER_PLASMA_PRO_CONCENT").append("\t")
                .append("TARGET_NAME").append("\t")
                .append("TARGET_NAME_APPROVAL_ID").append("\t")
                .append("TARGET_SPECIES").append("\t")
                .append("HUMAN_HOMOLOG_TARGET").append("\t")
                .append("HUMAN_HOMOLOG_TARGET_APPROVAL_ID").append("\t")
                .append("REFERENCE_SOURCE_TYPE").append("\t")
                .append("REFERENCE_SOURCE_NUMBER").append("\t")
                .append("REPORT_NUMBER").append("\t")
                .append("REPORT_DATE").append("\t")
                .append("LABORATORY_NAME").append("\t")
                .append("TEST_AGENT").append("\t")
                .append("TEST_AGENT_APPROVAL_ID").append("\t")
                .append("TEST_AGENT_CONCENTRATION").append("\t")
                .append("TEST_AGENT_CONCENTRATION_UNITS").append("\t")
                .append("RESULT_VALUE").append("\t")
                .append("RESULT_VALUE_UNITS").append("\t")
                .append("ASSAY_MODE").append("\t");

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

            /*
            if (s.productIngredientAllList.size() > 0) {
                for (int i = 0; i < s.productIngredientAllList.size(); i++) {

                    this.ingredientNumber = i;
                    int ingredNum = ingredientNumber + 1;

                    StringBuilder sb = new StringBuilder();

                    sb.append(ingredNum).append("\t");  // INGREDIENT_NUMBER

                    StringBuilder sbName = getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_NAME);
                    sb.append(sbName.toString()).append("\t");  // SUBSTANCE_NAME

                    StringBuilder sbApprovalId = getIngredientDetails(s,ProdDefaultColumns.APPROVAL_ID);
                    sb.append(sbApprovalId.toString()).append("\t");  // APPROVAL_ID

                    StringBuilder sbSubKey = getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_KEY);
                    sb.append(sbSubKey.toString()).append("\t");  // SUBSTANCE_KEY

                    StringBuilder sbIngType = getIngredientDetails(s,ProdDefaultColumns.INGREDIENT_TYPE);
                    sb.append(sbIngType.toString()).append("\t");  // INGREDIENT_TYPE

                    StringBuilder sbActMoietyName = getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_NAME);
                    sb.append(sbActMoietyName.toString()).append("\t");  // ACTIVE_MOIETY_NAME

                    StringBuilder sbActMoietyUnii = getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_UNII);
                    sb.append(sbActMoietyUnii.toString()).append("\t");  // ACTIVE_MOIETY_UNII

                    sb.append((s.productNDC != null) ? s.productNDC : "").append("\t"); // PRODUCT_ID

                    StringBuilder sbProdName = getProductNameDetails(s,ProdDefaultColumns.PRODUCT_NAME);
                    sb.append(sbProdName.toString()).append("\t");  // PRODUCT_NAME

                    sb.append((s.nonProprietaryName != null) ? s.nonProprietaryName : "").append("\t");  // NON_PROPRIETARY_NAME

                    sb.append((s.status != null) ? s.status : "").append("\t"); // STATUS

                    sb.append((s.productType != null) ? s.productType : "").append("\t");  // PRODUCT_TYPE

                    sb.append((s.routeName != null) ? s.routeName : "").append("\t");  // ROUTE_OF_ADMINISTRATOR

                    StringBuilder sbDosageForm = getIngredientDetails(s,ProdDefaultColumns.DOSAGE_FORM_NAME);
                    sb.append(sbDosageForm.toString()).append("\t");  // DOSAGE_FORM_NAME

                    sb.append((s.marketingCategoryName != null) ? s.marketingCategoryName : "").append("\t");  // MARKETING_CATEGORY_NAME

                    sb.append((s.appTypeNumber != null) ? s.appTypeNumber : "").append("\t");  // APPLICATION_NUMBER

                    sb.append((s.isListed != null) ? s.isListed : "").append("\t");  // IS_LISTED

                    StringBuilder sbLabelerName = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_NAME);
                    sb.append(sbLabelerName.toString()).append("\t");  // LABELER_NAME

                    StringBuilder sbLabelerDuns = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_DUNS);
                    sb.append(sbLabelerDuns.toString()).append("\t");  // LABELER_DUNS

                    StringBuilder sbLabelerAdd = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ADDRESS);
                    sb.append(sbLabelerAdd.toString()).append("\t");  // LABELER_ADDRESS

                    StringBuilder sbLabelerCity = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_CITY);
                    sb.append(sbLabelerCity.toString()).append("\t");  // LABELER_CITY

                    StringBuilder sbLabelerState = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_STATE);
                    sb.append(sbLabelerState.toString()).append("\t");  // LABELER_STATE

                    StringBuilder sbLabelerZip = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ZIP);
                    sb.append(sbLabelerZip.toString()).append("\t");  // LABELER_ZIP

                    StringBuilder sbLabelerCountry = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_COUNTRY);
                    sb.append(sbLabelerCountry.toString()).append("\t");  // LABELER_COUNTRY

                    sb.append((s.provenance != null) ? s.provenance : "").append("\t");  // PROVENANCE

                    bw.write(sb.toString());
                    bw.newLine();


                } // for ProductIngredient
            } // Ingredient size > 0 */
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

            sb.append((a.assayId != null) ? a.assayId : "").append("\t");  // ASSAY_ID

            // Write to buffer
            this.bw.write(sb.toString());
            this.bw.newLine();

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }

    /*
    private static StringBuilder getProductNameDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (s.productNameAllList.size() > 0) {

            for (ProductNameAll prodName : s.productNameAllList) {
                if (sb.length() != 0) {
                    sb.append("|");
                }
                switch (fieldName) {
                    case PRODUCT_NAME:
                        sb.append((prodName.productName != null) ? prodName.productName : "(No Product Name)");
                        break;
                    default:
                        break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getProductCompanyDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (s.productCompanyAllList.size() > 0) {

            for (ProductCompanyAll prodComp : s.productCompanyAllList) {
                if (sb.length() != 0) {
                    sb.append("|");
                }
                switch (fieldName) {
                    case LABELER_NAME:
                        sb.append((prodComp.labelerName != null) ? prodComp.labelerName : "");
                        break;
                    case LABELER_DUNS:
                        sb.append((prodComp.labelerDuns != null) ? prodComp.labelerDuns : "");
                        break;
                    case LABELER_ADDRESS:
                        sb.append((prodComp.address != null) ? prodComp.address : "");
                        break;
                    case LABELER_CITY:
                        sb.append((prodComp.city != null) ? prodComp.city : "");
                        break;
                    case LABELER_STATE:
                        sb.append((prodComp.state != null) ? prodComp.state : "");
                        break;
                    case LABELER_ZIP:
                        sb.append((prodComp.zip != null) ? prodComp.zip : "");
                        break;
                    case LABELER_COUNTRY:
                        sb.append((s.countryWithoutCode != null) ? s.countryWithoutCode : "");
                        break;

                    default:
                        break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getIngredientDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        try {
            if (s.productIngredientAllList.size() > 0) {

                ProductIngredientAll ingred = s.productIngredientAllList.get(ingredientNumber);

                switch (fieldName) {
                    case SUBSTANCE_NAME:
                        sb.append((ingred.substanceName != null) ? ingred.substanceName : "");
                        break;
                    case APPROVAL_ID:
                        sb.append((ingred.substanceApprovalId != null) ? ingred.substanceApprovalId : "");
                        break;
                    case SUBSTANCE_KEY:
                        sb.append((ingred.substanceKey != null) ? ingred.substanceKey : "");
                        break;
                    case INGREDIENT_TYPE:
                        sb.append((ingred.ingredientType != null) ? ingred.ingredientType : "");
                        break;
                    case ACTIVE_MOIETY_NAME:
                        sb.append((ingred.activeMoietyName != null) ? ingred.activeMoietyName : "");
                        break;
                    case ACTIVE_MOIETY_UNII:
                        sb.append((ingred.activeMoietyUnii != null) ? ingred.activeMoietyUnii : "");
                        break;
                    case DOSAGE_FORM_NAME:
                        sb.append((ingred.dosageFormName != null) ? ingred.dosageFormName : "");
                        break;
                    default:
                        break;
                }
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

        return sb;
    }
    */

}