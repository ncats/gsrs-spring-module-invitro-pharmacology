package gov.hhs.gsrs.invitropharmacology.models;


import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.IndexableRoot;
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
@Table(name="GSRS_INVITRO_TEST_AGENT")
public class InvitroTestAgent extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroTestAgentSeq", sequenceName="GSRS_INVITRO_TEST_AGENT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroTestAgentSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Test Agent", sortable = true)
    @Column(name="TEST_AGENT", length=1000)
    public String testAgent;

    @Indexable(suggest = true, facet=true, name= "Test Agent Approval ID", sortable = true)
    @Column(name="TEST_AGENT_APPROVAL_ID")
    public String testAgentApprovalId;

    @Column(name="TEST_AGENT_SUBSTANCE_UUID")
    public String testAgentSubstanceUuid;

    @Column(name="TEST_AGENT_SMILES_STRING")
    public String testAgentSmileString;

    @Column(name="TEST_AGENT_MOLECULAR_FORMULA_WT")
    public String testAgentMolecularFormulaWeight;

    @Indexable(suggest = true, facet=true, name= "Test Agent Active Moiety", sortable = true)
    @Column(name="ACTIVE_MOIETY")
    public String activeMoiety;

    @Indexable(suggest = true, facet=true, name= "Test Agent Active Moiety Approval ID", sortable = true)
    @Column(name="ACTIVE_MOIETY_APPROVAL_ID")
    public String activeMoietyApprovalId;

    @Indexable(suggest = true, facet=true, name= "CAS Register Number", sortable = true)
    @Column(name="CAS_REGISTRY_NUMBER")
    public String casRegistryNumber;

    @Indexable(suggest = true, facet=true, name= "Batch Number", sortable = true)
    @Column(name="BATCH_NUMBER")
    public String batchNumber;

    @Indexable(suggest = true, facet=true, name= "Purity", sortable = true)
    @Column(name="PURITY")
    public String purity;

    @Column(name="VEHICLE_COMPOSITION")
    public String vehicleComposition;
    
    public InvitroTestAgent () {}

}
