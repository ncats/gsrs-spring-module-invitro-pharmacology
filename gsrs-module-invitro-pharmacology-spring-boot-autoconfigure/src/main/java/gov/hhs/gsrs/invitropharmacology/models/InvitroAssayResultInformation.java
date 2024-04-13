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

    @Column(name = "BATCH_NUMBER")
    public String batchNumber;

    @Column(name = "PURITY_PERCENT")
    public String purityPercent;

    @Column(name = "VEHICLE_COMPOSITION")
    public String vehicleComposition;

    public InvitroAssayResultInformation() {
    }

    // Set Child for InvitroAssayResultInformation
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
}
