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
    public String summaryTestAgent;

    @Indexable(suggest = true, facet=true, name= "Summary Target Name", sortable = true)
    @Column(name="TARGET_NAME", length=2000)
    public String summaryTargetName;

    @Indexable(suggest = true, facet=true, name= "Relationship Type", sortable = true)
    @Column(name="RELATIONSHIP_TYPE")
    public String relationshipType;

    @Indexable(suggest = true, facet=true, name= "Summary Result Type", sortable = true)
    @Column(name="RESULT_TYPE")
    public String resultType;

    @Column(name="RESULT")
    public String result;

    @Column(name="RESULT_UNITS")
    public String resultUnits;

    @Column(name="AMOUNT_TYPE")
    public String amountType;

    @Column(name="AMOUNT_AVERAGE")
    public Double amountAverage;

    @Column(name="AMOUNT_LOW")
    public Double amountLow;

    @Column(name="AMOUNT_HIGH")
    public Double amountHigh;

    @Column(name="AMOUNT_UNITS")
    public String amountUnits;

    public InvitroSummary () {}

}
