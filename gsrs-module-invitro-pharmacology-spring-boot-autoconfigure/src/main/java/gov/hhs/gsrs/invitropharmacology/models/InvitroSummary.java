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

import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_SUMMARY")
public class InvitroSummary extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroSummarySeq", sequenceName="GSRS_INVITRO_SUMMARY_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroSummarySeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Summary Test Agent", sortable = true)
    @Column(name="TEST_AGENT", length=2000)
    public String testAgent;

    @Indexable(suggest = true, facet=true, name= "Relationship Type", sortable = true)
    @Column(name="RELATIONSHIP_TYPE")
    public String relationshipType;

    @Indexable(suggest = true, facet=true, name= "Interaction Type", sortable = true)
    @Column(name="INTERACTION_TYPE")
    public String interactionType;

    @Indexable(suggest = true, facet=true, name= "Summary Result Type", sortable = true)
    @Column(name="RESULT_TYPE")
    public String resultType;

    @Column(name="RESULT_VALUE_AVERAGE")
    public String resultValueAverage;

    @Column(name="RESULT_VALUE_LOW")
    public String resultValueLow;

    @Column(name="RESULT_VALUE_HIGH")
    public String resultValueHigh;

    @Column(name="RESULT_VALUE_UNITS")
    public String resultValueUnits;

    @Column(name="COMMENTS", length=4000)
    public String comments;

    public InvitroSummary () {}

    // Set Parent Class, InvitroAssayInformation
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="INVITRO_ASSAY_INFO_ID", referencedColumnName="ID")
    public InvitroAssayInformation ownerOfAssayInfo;

    public void setOwner(InvitroAssayInformation invitroAssayInformation) {
        this.ownerOfAssayInfo = invitroAssayInformation;
    }
}
