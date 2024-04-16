package gov.hhs.gsrs.invitropharmacology.models;

import gsrs.model.AbstractGsrsEntity;
import gsrs.security.GsrsSecurityUtils;
import ix.core.models.Indexable;
import ix.core.models.Principal;
import ix.core.models.UserProfile;
import gsrs.ForceUpdateDirtyMakerMixin;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public class InvitroPharmacologyCommanData extends AbstractGsrsEntity implements ForceUpdateDirtyMakerMixin{

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Record Created Date", sortable=true)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Indexable(facet = true, name = "Record Created By")
    @Column(name = "CREATED_BY")
    private String createdBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Record Modified Date", sortable=true)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Indexable(name = "Record Modified By")
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Version
    @Column(name = "INTERNAL_VERSION")
    public Long internalVersion;

    public InvitroPharmacologyCommanData () {
    }

    @PrePersist
    public void prePersist() {
        try {
            UserProfile profile = (UserProfile) GsrsSecurityUtils.getCurrentUser();
            if (profile != null) {
                Principal p = profile.user;
                if (p != null) {
                    this.createdBy = p.username;
                    this.modifiedBy = p.username;
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PreUpdate
    public void preUpdate() {
        try {
            UserProfile profile = (UserProfile) GsrsSecurityUtils.getCurrentUser();
            if (profile != null) {
                Principal p = profile.user;
                if (p != null) {
                    this.modifiedBy = p.username;
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getCreatedBy () {
        return this.createdBy;
    }

    public Date getCreatedDate() {
        //Get from Database
        return this.createdDate;
    }

    public String getModifiedBy () {
        return this.modifiedBy;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public String convertDateToString(Date dtDate) {

        String strDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if (dtDate != null) {
                strDate = df.format(dtDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return strDate;
    }

    public Date convertStringToDate(String strDate) {

        Date dtDate = null;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            if ((strDate != null) && (strDate.length() > 0)) {
                dtDate = df.parse(strDate);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return dtDate;
    }

}
