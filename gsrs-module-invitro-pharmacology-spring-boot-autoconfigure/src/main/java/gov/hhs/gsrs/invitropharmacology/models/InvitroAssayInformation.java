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
import java.util.UUID;

@IndexableRoot
@Data
@Entity
@Table(name="GSRS_INVITRO_ASSAY_INFORMATION")
public class InvitroAssayInformation extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroAssayInfoSeq", sequenceName="GSRS_INVITRO_ASSAY_INFO_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroAssayInfoSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Assay ID", sortable = true)
    @Column(name="ASSAY_ID")
    public String assayId;

    @Indexable(suggest = true, facet=true, name= "External Assay ID", sortable = true)
    @Column(name="EXTERNAL_ASSAY_ID")
    public String externalAssayId;

    @Indexable(suggest = true, facet=true, name= "External Assay Source", sortable = true)
    @Column(name="EXTERNAL_ASSAY_SOURCE")
    public String externalAssaySource;

    @Column(name="EXTERNAL_ASSAY_REFERENCE")
    public String externalAssayReference;

    @Column(name="EXTERNAL_ASSAY_REFERENCE_URL", length=1000)
    public String externalAssayReferenceUrl;

    @Indexable(suggest = true, facet=true, name= "Assay Title", sortable = true)
    @Column(name="ASSAY_TITLE")
    public String assayTitle;

    @Indexable(suggest = true, facet=true, name= "Assay Format", sortable = true)
    @Column(name="ASSAY_FORMAT")
    public String assayFormat;

    @Indexable(suggest = true, facet=true, name= "Bioassay Type", sortable = true)
    @Column(name="BIOASSAY_TYPE")
    public String bioassayType;

    @Indexable(suggest = true, facet=true, name= "Bioassay Class", sortable = true)
    @Column(name="BIOASSAY_CLASS")
    public String bioassayClass;

    @Indexable(suggest = true, facet=true, name= "Study Type", sortable = true)
    @Column(name="STUDY_TYPE")
    public String studyType;

    @Indexable(suggest = true, facet=true, name= "Detection Method", sortable = true)
    @Column(name="DETECTION_METHOD")
    public String detectionMethod;

    @Column(name="BUFFER_PLASMA_PRO_CONCENT")
    public String bufferPlasmaProteinConcent;

    @Indexable(suggest = true, facet=true, name= "Presentation Type", sortable = true)
    @Column(name="PRESENTATION_TYPE")
    public String presentationType;

    @Column(name="PRESENTATION")
    public String presentation;

    @Indexable(suggest = true, facet=true, name= "Target Name", sortable = true)
    @Column(name="TARGET_NAME", length=1000)
    public String targetName;

    @Indexable(suggest = true, name= "Target Name Approval ID")
    @Column(name="TARGET_NAME_APPROVAL_ID")
    public String targetNameApprovalId;

    @Indexable(suggest = true, facet=true, name= "Target Species", sortable = true)
    @Column(name="TARGET_SPECIES")
    public String targetSpecies;

    @Indexable(suggest = true, facet=true, name= "Human Homolog Target", sortable = true)
    @Column(name="HUMAN_HOMOLOG_TARGET", length=1000)
    public String humanHomologTarget;

    @Indexable(suggest = true, facet=true, name= "Human Homolog Target Approval ID", sortable = true)
    @Column(name="HUMAN_HOMOLOG_TARGET_APPROVAL_ID")
    public String humanHomologTargetApprovalId;

    @Indexable(suggest = true, facet=true, name= "Ligand (Substrate)", sortable = true)
    @Column(name="LIGAND_SUBSTRATE", length=1000)
    public String ligandSubstrate;

    @Indexable(suggest = true, name= "Ligand (Substrate) Approval ID")
    @Column(name="LIGAND_SUBSTRATE_APPROVAL_ID")
    public String ligandSubstrateApprovalId;

    @Column(name="LIGAND_SUBSTRATE_CONCENT")
    public String ligandSubstrateConcentration;

    @Column(name="LIGAND_SUBSTRATE_CONCENT_UNITS")
    public String ligandSubstrateConcentrationUnits;

    public InvitroAssayInformation() {}

    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated() {
        return "Not Deprecated";
    }

    // Set Child Class, InvitroAssayScreening
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ownerOfAssayInfo")
    public List<InvitroAssayScreening> invitroAssayScreenings = new ArrayList<InvitroAssayScreening>();

    public void setInvitroAssayScreenings(List<InvitroAssayScreening> invitroAssayScreenings) {
        this.invitroAssayScreenings = invitroAssayScreenings;
        if (invitroAssayScreenings != null) {
            for (InvitroAssayScreening invitro : invitroAssayScreenings)
            {
                invitro.setOwner(this);
            }
        }
    }

}
