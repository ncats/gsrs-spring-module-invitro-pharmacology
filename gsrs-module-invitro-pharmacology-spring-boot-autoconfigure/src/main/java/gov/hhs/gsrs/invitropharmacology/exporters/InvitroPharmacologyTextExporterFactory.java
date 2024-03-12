package gov.hhs.gsrs.invitropharmacology.exporters;

import gov.hhs.gsrs.invitropharmacology.controllers.*;

import ix.ginas.exporters.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;;
import java.util.*;

public class InvitroPharmacologyTextExporterFactory implements ExporterFactory {

    OutputFormat format = new OutputFormat("txt", "Tab-delimited (.txt)");

    @Override
    public boolean supports(ExporterFactory.Parameters params) {
        return params.getFormat().equals(format);
    }

    @Override
    public Set<OutputFormat> getSupportedFormats() {
        return Collections.singleton(format);
    }

    @Override
    public InvitroPharmacologyTextExporter createNewExporter(OutputStream out, Parameters params) throws IOException {
        return new InvitroPharmacologyTextExporter(out, params);
    }

}