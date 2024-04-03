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

import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@SingleParent
@Data
@Entity
@Table(name = "GSRS_INVITRO_CONTROL")
public class InvitroControl extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name = "invitroControlSeq", sequenceName = "GSRS_INVITRO_CONTROL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroControlSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable(suggest = true, facet = true, name = "Control", sortable = true)
    @Column(name = "CONTROL", length = 2000)
    public String control;

    @Indexable(suggest = true, facet = true, name = "Control Approval ID", sortable = true)
    @Column(name = "CONTROL_APPROVAL_ID")
    public String controlApprovalId;

    @Indexable(suggest = true, facet = true, name = "Control Type", sortable = true)
    @Column(name = "CONTROL_TYPE")
    public String controlType;

    @Column(name = "CONTROL_REFERENCE_VALUE")
    public String controlReferenceValue;

    @Column(name = "CONTROL_REFERENCE_VALUE_UNITS")
    public String controlReferenceValueUnits;

    @Indexable(suggest = true, facet = true, name = "Result Type", sortable = true)
    @Column(name = "RESULT_TYPE")
    public String resultType;

    public InvitroControl() {
    }

    // Set Parent Class, InvitroAssayScreening
    @Indexable(indexed = false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "INVITRO_SCREENING_ID", referencedColumnName = "ID")
    public InvitroAssayScreening owner;

    public void setOwner(InvitroAssayScreening invitroAssayScreening) {
        this.owner = invitroAssayScreening;
    }
}
