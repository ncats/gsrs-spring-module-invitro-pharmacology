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
@Table(name="GSRS_INVITRO_REFERENCE")
public class InvitroReference extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroRefSeq", sequenceName="GSRS_INVITRO_REFERENCE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroRefSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Reference Source Type", sortable = true)
    @Column(name="REFERENCE_SOURCE_TYPE")
    public String referenceSourceType;

    @Indexable(suggest = true, facet=true, name= "Reference Source", sortable = true)
    @Column(name="REFERENCE_SOURCE")
    public String referenceSource;

    @Column(name="DIGITAL_OBJECT_IDENTIFIER", length=1000)
    public String digitalObjectIdentifier;

    public InvitroReference () {}

    @Basic(fetch = FetchType.EAGER)
    @JoinColumn(name = "INVITRO_SPONSOR_ID", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.PERSIST)
    public InvitroSponsor invitroSponsor;

}
