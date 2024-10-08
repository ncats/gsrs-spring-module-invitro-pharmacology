package gov.hhs.gsrs.invitropharmacology.exporters;

import gov.hhs.gsrs.invitropharmacology.controllers.*;
import gov.hhs.gsrs.invitropharmacology.services.SubstanceApiService;

import ix.ginas.exporters.*;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;;
import java.util.*;

public class InvitroPharmacologyExporterFactory implements ExporterFactory {

    @Autowired
    private SubstanceApiService substanceApiService;

    private static final Set<OutputFormat> FORMATS;

    static{
        Set<OutputFormat> set = new LinkedHashSet<>();
    //    set.add(SpreadsheetFormat.TSV);
    //    set.add(SpreadsheetFormat.CSV);
        set.add(SpreadsheetFormat.XLSX);

        FORMATS = Collections.unmodifiableSet(set);
    }

    @Override
    public Set<OutputFormat> getSupportedFormats() {
        return FORMATS;
    }

    @Override
    public boolean supports(Parameters params) {
        return params.getFormat() instanceof SpreadsheetFormat;
    }

    @Override
    public InvitroPharmacologyExporter createNewExporter(OutputStream out, Parameters params) throws IOException {

        if (substanceApiService == null) {
            AutowireHelper.getInstance().autowire(this);
        }

        SpreadsheetFormat format = SpreadsheetFormat.XLSX;
        Spreadsheet spreadsheet = format.createSpreadsheet(out);

        InvitroPharmacologyExporter.Builder builder = new InvitroPharmacologyExporter.Builder(spreadsheet);

        configure(builder, params);

        return builder.build(substanceApiService);
    }
    
    protected void configure(InvitroPharmacologyExporter.Builder builder, Parameters params){
        builder.includePublicDataOnly(params.publicOnly());
    }

}