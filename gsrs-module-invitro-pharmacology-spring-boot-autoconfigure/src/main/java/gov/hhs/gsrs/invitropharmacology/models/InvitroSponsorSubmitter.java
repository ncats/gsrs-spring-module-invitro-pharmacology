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

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_SPON_SUBMITTER")
public class InvitroSponsorSubmitter extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroSubmitterSeq", sequenceName="GSRS_INVITRO_S_SUBMITTER_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroSubmitterSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Sponsor Report Submitter Name", sortable = true)
    @Column(name="SPONSOR_REP_SUBMITTER_NAME")
    public String sponsorReportSubmitterName;

    @Indexable(suggest = true, facet=true, name= "Sponsor Report Submitter Title", sortable = true)
    @Column(name="SPONSOR_REP_SUBMITTER_TITLE")
    public String sponsorReportSubmitterTitle;

    @Column(name="SPONSOR_REP_SUBMITTER_AFFIL")
    public String sponsorReportSubmitterAffiliation;

    @Column(name="SPONSOR_REP_SUBMITTER_EMAIL")
    public String sponsorReportSubmitterEmail;

    @Column(name="SPONSOR_REP_SUBMITTER_PHONE")
    public String sponsorReportSubmitterPhoneNumber;

    @Column(name="SP_REP_SUBMITTER_ASSAY_TYPE")
    public String sponsorReportSubmitterAssayType;

    public InvitroSponsorSubmitter () {}

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="GSRS_INVITRO_SUB_REPORT_DET", joinColumns = @JoinColumn(name = "INVITRO_SPON_SUBMITTER_ID"),
            inverseJoinColumns = @JoinColumn(name = "INVITRO_SUBMITTER_REPORT_ID"))
    public List<InvitroSubmitterReport> invitroSubmitterReports = new ArrayList<>();

}
