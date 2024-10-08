package gov.hhs.gsrs.invitropharmacology.models;

import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@SingleParent
@Data
@Entity
@Table(name = "GSRS_INVITRO_ASSAY_RESULT")
public class InvitroAssayResult extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name = "invitroResultSeq", sequenceName = "GSRS_INVITRO_ASSAY_RESULT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroResultSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST_AGENT_CONCENTRATION")
    public Double testAgentConcentration;

    @Column(name = "TEST_AGENT_CONCENTRATION_UNITS")
    public String testAgentConcentrationUnits;

    @Column(name = "RESULT_VALUE")
    public Double resultValue;

    @Column(name = "RESULT_VALUE_UNITS")
    public String resultValueUnits;

    @Column(name = "LIGAND_SUBSTRATE_CONCENT")
    public Double ligandSubstrateConcentration;

    @Column(name = "LIGAND_SUBSTRATE_CONCENT_UNITS")
    public String ligandSubstrateConcentrationUnits;

    @Column(name = "PLASMA_PROTEIN_ADDED")
    public Boolean plasmaProteinAdded;

    @Column(name = "PROTEIN")
    public String protein;

    @Column(name = "PLASMA_PROTEIN_CONCENT")
    public String plasmaProteinConcentration;

    @Column(name = "PLASMA_PROTEIN_CONCENT_UNITS")
    public String plasmaProteinConcentrationUnits;

    @Indexable(suggest = true, facet=true, name= "Test Date", sortable = true)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name = "TEST_DATE")
    public Date testDate;

    @Column(name = "NUMBER_OF_TESTS")
    public String numberOfTests;

    @Column(name = "DATA_TYPE")
    public String dataType;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

    @Column(name = "ASSAY_MEASUREMENT", length=4000)
    public String assayMeasurement;

    public InvitroAssayResult() {
    }

    public void setIsDirtyToFields() {
        this.setIsDirty("testAgentConcentration");
    }
}
