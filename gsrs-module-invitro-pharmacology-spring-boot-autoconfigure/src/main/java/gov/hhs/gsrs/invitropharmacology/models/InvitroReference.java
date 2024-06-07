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
    @Column(name="SOURCE_TYPE")
    public String sourceType;

    @Indexable(suggest = true, facet=true, name= "Reference Source Id", sortable = true)
    @Column(name="SOURCE_ID")
    public String sourceId;

    @Indexable(suggest = true, facet=true, name= "Reference Source Citation", sortable = true)
    @Column(name="SOURCE_CITATION", length=1000)
    public String sourceCitation;

    @Indexable(suggest = true, facet=true, name= "Reference Source Url", sortable = true)
    @Column(name="SOURCE_URL", length=1000)
    public String sourceUrl;

    @Column(name="DIGITAL_OBJECT_IDENTIFIER", length=1000)
    public String digitalObjectIdentifier;

    @Column(name="TAGS", length=1000)
    public String tags;

    @Column(name="RECORD_ACCESS", length=1000)
    public String recordAccess;

    @Column(name="UPLOADED_FILE", length=1000)
    public String uploadedFile;

    @Column(name="PUBLIC_DOMAIN")
    public boolean publicDomain;

    @Column(name="PRIMARY_REFERENCE")
    public Boolean primaryReference;

    public InvitroReference () {}

    // Set Parent Class, InvitroAssayResultInformation
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="INVITRO_RESULT_INFO_ID", referencedColumnName="ID")
    public InvitroAssayResultInformation owner;

}
