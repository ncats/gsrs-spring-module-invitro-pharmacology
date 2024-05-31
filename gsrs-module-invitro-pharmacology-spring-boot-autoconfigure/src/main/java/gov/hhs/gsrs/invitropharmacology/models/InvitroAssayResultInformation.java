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
import java.util.Objects;

@SingleParent
@Data
@Entity
@Table(name = "GSRS_INVITRO_RESULT_INFORMATION")
public class InvitroAssayResultInformation extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name = "invitroResultInfoSeq", sequenceName = "GSRS_INVITRO_RESULT_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroResultInfoSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "BATCH_NUMBER")
    public String batchNumber;

    @Column(name = "TEST_AGENT", length=2000)
    public String testAgent;

    @Column(name = "TEST_AGENT_SUBSTANCE_UUID")
    public String testAgentSubstanceUuid;

    public InvitroAssayResultInformation() {
    }

    // Set Child for InvitroAssayScreening
    @JsonIgnore
    @Indexable(indexed=false)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "invitroAssayResultInformation")
    public List<InvitroAssayScreening> invitroAssayScreenings = new ArrayList<InvitroAssayScreening>();

    public void setInvitroAssayScreenings(List<InvitroAssayScreening> invitroAssayScreenings) {
        this.invitroAssayScreenings = invitroAssayScreenings;
        if (invitroAssayScreenings != null) {
            for (InvitroAssayScreening invitro : invitroAssayScreenings)
            {
                invitro.setInvitroAssayResultInformation(this);
            }
        }
    }

    public InvitroAssayScreening addInvitroAssayScreeningChild(InvitroAssayScreening invitroAssayScreening) {
        this.invitroAssayScreenings.add(invitroAssayScreening);
        invitroAssayScreening.setInvitroAssayResultInformation(this);
        this.setIsDirty("invitroAssayScreenings");
        return invitroAssayScreening;
    }

    // Set Child for InvitroAssayScreening
    @Indexable(indexed=false)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<InvitroReference> invitroReferences = new ArrayList<InvitroReference>();

    public void setInvitroReferences(List<InvitroReference> invitroReferences) {
        this.invitroReferences = invitroReferences;
        if (invitroReferences != null) {
            for (InvitroReference invitro : invitroReferences)
            {
                invitro.setOwner(this);
            }
        }
    }

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_LABORATORY_ID", referencedColumnName = "ID")
    public InvitroLaboratory invitroLaboratory;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_SPONSOR_ID", referencedColumnName = "ID")
    public InvitroSponsor invitroSponsor;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_SPONSOR_REPORT_ID", referencedColumnName = "ID")
    public InvitroSponsorReport invitroSponsorReport;

   // @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="INVITRO_TEST_AGENT_ID", referencedColumnName = "ID")
    public InvitroTestAgent invitroTestAgent;

    /*
    public void setIsDirtyToFields() {
        this.setIsDirty("batchNumber");
    } */
}
