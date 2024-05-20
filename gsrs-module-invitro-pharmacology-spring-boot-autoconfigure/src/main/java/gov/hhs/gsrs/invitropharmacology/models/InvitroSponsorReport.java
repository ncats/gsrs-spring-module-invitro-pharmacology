package gov.hhs.gsrs.invitropharmacology.models;

import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SingleParent
@Data
@Entity
@Table(name="GSRS_INVITRO_SPONSOR_REPORT")
public class InvitroSponsorReport extends InvitroPharmacologyCommanData {

    @Id
    @SequenceGenerator(name="invitroSubReportSeq", sequenceName="GSRS_INVITRO_SPONSOR_REPORT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "invitroSubReportSeq")
    @Column(name="ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Report Number", sortable = true)
    @Column(name="REPORT_NUMBER")
    public String reportNumber;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="REPORT_DATE")
    public Date reportDate;

    public InvitroSponsorReport () {}

    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="GSRS_INVITRO_SPONSOR_SUB_REPORT_DET", joinColumns = @JoinColumn(name = "INVITRO_SPONSOR_REPORT_ID"),
            inverseJoinColumns = @JoinColumn(name = "INVITRO_SPONSOR_SUBMITTER_ID"))
    public List<InvitroSponsorSubmitter> invitroSponsorSubmitters = new ArrayList<>();

    public void setIsDirtyToFields() {
        this.setIsDirty("reportNumber");
        this.setIsDirty("invitroSponsorSubmitters");
    }
}
