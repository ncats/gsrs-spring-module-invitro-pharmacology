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
@Table(name="GSRS_INVITRO_SPONSOR")
public class InvitroSponsor extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroSponsorSeq", sequenceName="GSRS_INVITRO_SPONSOR_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroSponsorSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Sponsor Contact Name", sortable = true)
    @Column(name="SPONSOR_CONTACT_NAME")
    public String sponsorContactName;

    @Indexable(suggest = true, facet=true, name= "Sponsor Affiliation", sortable = true)
    @Column(name="SPONSOR_AFFILIATION")
    public String sponsorAffiliation;

    @Column(name="SPONSOR_STREET_ADDRESS", length=1000)
    public String sponsorStreetAddress;

    @Indexable(suggest = true, facet=true, name= "Sponsor City", sortable = true)
    @Column(name="SPONSOR_CITY")
    public String sponsorCity;

    @Indexable(suggest = true, facet=true, name= "Sponsor State", sortable = true)
    @Column(name="SPONSOR_STATE")
    public String sponsorState;

    @Column(name="SPONSOR_ZIPCODE")
    public String sponsorZipcode;

    @Indexable(suggest = true, facet=true, name= "Sponsor Country", sortable = true)
    @Column(name="SPONSOR_COUNTRY")
    public String sponsorCountry;

    public InvitroSponsor () {}

}
