package gov.hhs.gsrs.invitropharmacology.models;

import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "TEST_CONCENTRATION_ACTIVE_MOIETY")
    public Double testConcentrationActiveMoiety;

    @Column(name = "TEST_CONCENTRATION_UNITS_MOIETY")
    public String testConcentrationUnitsActiveMoiety;

    @Column(name = "RESULT_VALUE")
    public Double resultValue;

    @Column(name = "RESULT_VALUE_UNITS")
    public String resultValueUnits;

    @Column(name = "ASSAY_MODE")
    public String assayMode;

    @Column(name = "COMMENTS", length=4000)
    public String comments;

    @Column(name = "ASSAY_MEASUREMENT", length=4000)
    public String assayMeasurement;

    @Column(name = "LIGAND_SUBSTRATE_CONCENT")
    public Double ligandSubstrateConcentration;

    @Column(name = "LIGAND_SUBSTRATE_CONCENT_UNITS")
    public String ligandSubstrateConcentrationUnits;

    @Column(name = "NUMBER_OF_TESTS")
    public String numberOfTests;

    @Column(name = "DATA_TYPE")
    public String dataType;

    @Column(name = "PLASMA_PROTEIN_ADDED")
    public String plasmaProteinAdded;

    public InvitroAssayResult() {
    }

}
