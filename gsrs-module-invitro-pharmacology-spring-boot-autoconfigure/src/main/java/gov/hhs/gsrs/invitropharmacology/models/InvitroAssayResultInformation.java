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
@Table(name = "GSRS_INVITRO_RESULT_INFORMATION")
public class InvitroAssayResultInformation extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name = "invitroResultInfoSeq", sequenceName = "GSRS_INVITRO_RESULT_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroResultInfoSeq")
    @Column(name = "ID")
    public Long id;

    @Column(name = "TEST_AGENT_SUBSTANCE_UUID")
    public String testAgentSubstanceUuid;

    @Column(name = "TEST_AGENT", length=2000)
    public String testAgent;

    @Column(name = "PURITY_PERCENT")
    public String purityPercent;

    @Column(name = "VEHICLE_COMPOSITION")
    public String vehicleComposition;

    @Column(name = "ASSAY_SET")
    public String assaySet;

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

    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name="INVITRO_REFERENCE_ID")
    public InvitroReference invitroReference;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_SPONSOR_ID", referencedColumnName = "ID")
    public InvitroSponsor invitroSponsor;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_SPONSOR_REPORT_ID", referencedColumnName = "ID")
    public InvitroSponsorReport invitroSponsorReport;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "INVITRO_LABORATORY_ID", referencedColumnName = "ID")
    public InvitroLaboratory invitroLaboratory;
}
