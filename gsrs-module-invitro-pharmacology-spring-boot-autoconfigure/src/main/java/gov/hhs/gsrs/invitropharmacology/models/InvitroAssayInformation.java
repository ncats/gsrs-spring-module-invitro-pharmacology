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
import java.util.LinkedHashSet;
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

    @Indexable(suggest = true, facet=true, name= "External Assay Source", sortable = true)
    @Column(name="EXTERNAL_ASSAY_SOURCE")
    public String externalAssaySource;

    @Indexable(suggest = true, facet=true, name= "External Assay ID", sortable = true)
    @Column(name="EXTERNAL_ASSAY_ID")
    public String externalAssayId;

    @Column(name="EXTERNAL_ASSAY_REFERENCE_URL", length=1000)
    public String externalAssayReferenceUrl;

    @Indexable(suggest = true, facet=true, name= "Assay Title", sortable = true)
    @Column(name="ASSAY_TITLE", length=1000)
    public String assayTitle;

    @Indexable(suggest = true, facet=true, name= "Assay Format", sortable = true)
    @Column(name="ASSAY_FORMAT")
    public String assayFormat;

    @Column(name = "ASSAY_MODE")
    public String assayMode;

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

    @Indexable(suggest = true, facet=true, name= "Presentation Type", sortable = true)
    @Column(name="PRESENTATION_TYPE")
    public String presentationType;

    @Column(name="PRESENTATION")
    public String presentation;

    @Indexable(suggest = true, facet=true, name= "Assay Public Domain", sortable = true)
    @Column(name="PUBLIC_DOMAIN")
    public String publicDomain;

    @Indexable(suggest = true, facet=true, name= "Target Species", sortable = true)
    @Column(name="TARGET_SPECIES")
    public String targetSpecies;

    @Indexable(suggest = true, facet=true, name= "Target Name", sortable = true)
    @Column(name="TARGET_NAME", length=1000)
    public String targetName;

    @Indexable(suggest = true, name= "Target Name Approval ID")
    @Column(name="TARGET_NAME_APPROVAL_ID")
    public String targetNameApprovalId;

    @Column(name="TARGET_NAME_SUBSTANCE_UUID")
    public String targetNameSubstanceUuid;

    @Indexable(suggest = true, name= "Target Name Substance Key")
    @Column(name="TARGET_NAME_SUBSTANCE_KEY")
    public String targetNameSubstanceKey;

    @Column(name="TARGET_NAME_SUBSTANCE_KEY_TYPE")
    public String targetNameSubstanceKeyType;

    @Indexable(suggest = true, facet=true, name= "Human Homolog Target", sortable = true)
    @Column(name="HUMAN_HOMOLOG_TARGET", length=1000)
    public String humanHomologTarget;

    @Indexable(suggest = true, facet=true, name= "Human Homolog Target Approval ID", sortable = true)
    @Column(name="HUMAN_HOMOLOG_TARGET_APPROVAL_ID")
    public String humanHomologTargetApprovalId;

    @Indexable(suggest = true, facet=true, name= "Human Homolog Target Substance Key", sortable = true)
    @Column(name="HUMAN_HOMOLOG_TARGET_SUBSTANCE_KEY")
    public String humanHomologTargetSubstanceKey;

    @Column(name="HUMAN_HOMOLOG_TARGET_SUBSTANCE_KEY_TYPE")
    public String humanHomologTargetSubstanceKeyType;

    @Indexable(suggest = true, facet=true, name= "Ligand (Substrate)", sortable = true)
    @Column(name="LIGAND_SUBSTRATE", length=1000)
    public String ligandSubstrate;

    @Indexable(suggest = true, name= "Ligand (Substrate) Approval ID")
    @Column(name="LIGAND_SUBSTRATE_APPROVAL_ID")
    public String ligandSubstrateApprovalId;

    @Indexable(suggest = true, name= "Ligand (Substrate) Substance Key")
    @Column(name="LIGAND_SUBSTRATE_SUBSTANCE_KEY")
    public String ligandSubstrateSubstanceKey;

    @Column(name="LIGAND_SUBSTRATE_SUBSTANCE_KEY_TYPE")
    public String ligandSubstrateSubstanceKeyType;

    @Column(name="STANDARD_LIGAND_CONCENT")
    public String standardLigandSubstrateConcentration;
  
    @Column(name="STANDARD_LIGAND_CONCENT_UNITS")
    public String standardLigandSubstrateConcentrationUnits;

    public InvitroAssayInformation() {}

    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated() {
        return "Not Deprecated";
    }

    // Set Child for InvitroAssayScreening
    @ToString.Exclude
    @OrderBy("modifiedDate asc")
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
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

    // Many To Many, InvitroAssaySet
    //@Indexable(indexed=false)
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade= CascadeType.ALL)
    @JoinTable(name="GSRS_INVITRO_ASSAY_SET_DET", joinColumns = @JoinColumn(name = "INVITRO_ASSAY_INFO_ID "),
            inverseJoinColumns = @JoinColumn(name = "INVITRO_ASSAY_SET_ID"))
    public List<InvitroAssaySet> invitroAssaySets = new ArrayList<>();

    public void addInvitroAssaySet(InvitroAssaySet assaySet) {
        this.invitroAssaySets.add(assaySet);
    }

    @PreUpdate
    public void beforeUpdate(){
        if (invitroAssayScreenings.size() > 0) {

            for (int i = 0; i < invitroAssayScreenings.size(); i++) {

                InvitroAssayScreening screening = invitroAssayScreenings.get(i);
                if (screening != null) {

                    if (screening.invitroAssayResultInformation != null) {

                        // IMPORTANT NEED THIS. Set Dirty Fields, to save/update the fields into the database
                       // screening.setIsDirty("invitroAssayResultInformation");
                       // screening.setIsDirtyToFields();
                       // screening.invitroAssayResultInformation.setIsDirtyToFields();

                        /*
                        if (screening.invitroAssayResultInformation.invitroLaboratory != null) {
                            screening.invitroAssayResultInformation.invitroLaboratory.setIsDirtyToFields();
                        }

                        if (screening.invitroAssayResultInformation.invitroSponsor != null) {
                            screening.invitroAssayResultInformation.invitroSponsor.setIsDirtyToFields();
                        }
                        if (screening.invitroAssayResultInformation.invitroSponsorReport != null) {
                            screening.invitroAssayResultInformation.invitroSponsorReport.setIsDirtyToFields();
                        }

                        if (screening.invitroAssayResultInformation.invitroTestAgent != null) {
                            screening.invitroAssayResultInformation.invitroTestAgent.setIsDirtyToFields();
                        }

                        if (screening.invitroAssayResult != null) {
                            screening.invitroAssayResult.setIsDirtyToFields();
                        }
                        if (screening.invitroAssayResultInformation.id != null) {
                        }

                         */
                    } // if invitroAssayResultInformation is not null
                    //  }  // if screening id is null
                } // if screening is not null

            } // for
        } // if screening size > 0
    }

}


