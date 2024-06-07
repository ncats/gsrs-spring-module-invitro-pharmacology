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
@Table(name = "GSRS_INVITRO_ASSAY_ANALYTE")
public class InvitroAssayAnalyte extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name = "invitroAnalyteSeq", sequenceName = "GSRS_INVITRO_ASSAY_ANALYTE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroAnalyteSeq")
    @Column(name = "ID")
    public Long id;

    @Indexable(suggest = true, facet = true, name = "Assay Analyte", sortable = true)
    @Column(name = "ANALYTE", length = 2000)
    public String analyte;

    @Column(name = "ANALYTE_SUBSTANCE_KEY ")
    public String analyteSubstanceKey;

    @Column(name = "ANALYTE_SUBSTANCE_KEY_TYPE")
    public String analyteSubstanceKeyType;

    public InvitroAssayAnalyte() {
    }

    // Set Parent Class, InvitroAssayInformation
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name="INVITRO_ASSAY_INFO_ID", referencedColumnName="ID")
    public InvitroAssayInformation owner;

    public void setOwner(InvitroAssayInformation invitroAssayInformation) {
        this.owner = invitroAssayInformation;
    }
}
