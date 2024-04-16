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

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@IndexableRoot
@Data
@Entity
@Table(name="GSRS_INVITRO_SCREENING")
public class InvitroAssayScreening extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroAssayScreenSeq", sequenceName="GSRS_INVITRO_SCREENING_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroAssayScreenSeq")
    @Column(name="ID")
    public Long id;

    public InvitroAssayScreening() {}

    public Long getId() {
        return id;
    }

    @Column(name = "ASSAY_SET")
    public String assaySet;

    @Column(name = "TEST_AGENT_SUBSTANCE_UUID")
    public String testAgentSubstanceUuid;

    // Set Parent Class, InvitroAssayInformation
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="INVITRO_ASSAY_INFO_ID", referencedColumnName="ID")
    public InvitroAssayInformation owner;

    public void setOwner(InvitroAssayInformation invitroAssayInformation) {
        this.owner = invitroAssayInformation;
    }

    @Column(name="TESTING")
    public String testing;

    /*
    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name="INVITRO_REFERENCE_ID")
    public InvitroReference invitroReference;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_SPONSOR_REPORT_ID")
    public InvitroSponsorReport invitroSponsorReport;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_LABORATORY_ID")
    public InvitroLaboratory invitroLaboratory;

    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name="INVITRO_TEST_AGENT_ID")
    public InvitroTestAgent invitroTestAgent;
    */

    // Set Parent Class, InvitroResultInformation
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="INVITRO_RESULT_INFO_ID", referencedColumnName="ID")
    public InvitroAssayResultInformation invitroAssayResultInformation;

    public void setInvitroAssayResultInformation(InvitroAssayResultInformation invitroAssayResultInformation) {
        this.invitroAssayResultInformation = invitroAssayResultInformation;
    }

    /*
    // Set Child for InvitroAssayScreening
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<InvitroAssayResult> invitroAssayResults = new ArrayList<InvitroAssayResult>();

    public void setInvitroAssayResults(List<InvitroAssayResult> invitroAssayResults) {
        this.invitroAssayResults = invitroAssayResults;
        if (invitroAssayResults != null) {
            for (InvitroAssayResult invitro : invitroAssayResults)
            {
                invitro.setOwner(this);
            }
        }
    }

   */

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="INVITRO_RESULT_ID")
    public InvitroAssayResult invitroAssayResult;

    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name="INVITRO_SUMMARY_ID")
    public InvitroSummary invitroSummary;

    // Set Child Class, InvitroControl
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "owner")
    public List<InvitroControl> invitroControls = new ArrayList<InvitroControl>();

    public void setInvitroControls(List<InvitroControl> invitroControls) {
        this.invitroControls = invitroControls;
        if (invitroControls != null) {
            for (InvitroControl invitro : invitroControls)
            {
                invitro.setOwner(this);
            }
        }
    }

}
