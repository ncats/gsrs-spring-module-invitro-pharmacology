package gov.hhs.gsrs.invitropharmacology.models;

import ix.core.SingleParent;
import ix.core.models.Indexable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ix.core.models.ParentReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_LABORATORY")
public class InvitroLaboratory extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroLabSeq", sequenceName="GSRS_INVITRO_LABORATORY_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroLabSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Laboratory Name", sortable = true)
    @Column(name="LABORATORY_NAME")
    public String laboratoryName;

    @Indexable(suggest = true, facet=true, name= "Laboratory Affiliation", sortable = true)
    @Column(name="LABORATORY_AFFILIATION")
    public String laboratoryAffiliation;

    @Indexable(suggest = true, facet=true, name= "Laboratory Type", sortable = true)
    @Column(name="LABORATORY_TYPE")
    public String laboratoryType;

    @Column(name="LABORATORY_STREET_ADDRESS", length=1000)
    public String laboratoryStreetAddress;

    @Indexable(suggest = true, facet=true, name= "Laboratory City", sortable = true)
    @Column(name="LABORATORY_CITY")
    public String laboratoryCity;

    @Indexable(suggest = true, facet=true, name= "Laboratory State", sortable = true)
    @Column(name="LABORATORY_STATE")
    public String laboratoryState;

    @Column(name="LABORATORY_ZIPCODE")
    public String laboratoryZipcode;

    @Indexable(suggest = true, facet=true, name= "Laboratory Country", sortable = true)
    @Column(name="LABORATORY_COUNTRY")
    public String laboratoryCountry;

    public InvitroLaboratory () {}

}
