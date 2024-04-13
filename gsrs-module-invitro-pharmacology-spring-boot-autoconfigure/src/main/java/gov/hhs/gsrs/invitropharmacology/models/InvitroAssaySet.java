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

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_ASSAY_SET")
public class InvitroAssaySet extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroAssaySetSeq", sequenceName="GSRS_INVITRO_ASSAY_SET_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroAssaySetSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Assay Set", sortable = true)
    @Column(name="ASSAY_SET")
    public String assaySet;

    public InvitroAssaySet () {}

    // Many To Many, InvitroAssayInformation
    @Indexable(indexed=false)
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinTable(name="GSRS_INVITRO_ASSAY_SET_DET", joinColumns = @JoinColumn(name = "INVITRO_ASSAY_SET_ID"),
            inverseJoinColumns = @JoinColumn(name = "INVITRO_ASSAY_INFO_ID"))
    public List<InvitroAssayInformation> invitroAssayInformations = new ArrayList<>();
}
