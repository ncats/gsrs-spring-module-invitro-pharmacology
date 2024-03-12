package gov.hhs.gsrs.invitropharmacology.models;

import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_SUBMITTER_REPORT")
public class InvitroSubmitterReport extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroSubReportSeq", sequenceName="GSRS_INVITRO_SM_REPORT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroSubReportSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Report Number", sortable = true)
    @Column(name="REPORT_NUMBER")
    public String reportNumber;

    //@Indexable(suggest = true, facet=true, name= "Report Date", sortable = true)
    @Column(name="REPORT_DATE")
    public Date reportDate;

    public InvitroSubmitterReport () {}

    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="GSRS_INVITRO_SUB_REPORT_DET", joinColumns = @JoinColumn(name = "INVITRO_SUBMITTER_REPORT_ID"),
            inverseJoinColumns = @JoinColumn(name = "INVITRO_SPON_SUBMITTER_ID"))
    public List<InvitroSponsorSubmitter> invitroSponsorSubmitters = new ArrayList<>();

}
