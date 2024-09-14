package com.kit.grs.dto;


import com.kit.grs.common.Utils;
import com.kit.grs.util.CalendarUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplainHistory implements Serializable {

    Long complainId;
    String trackingNumber;
    String currentStatus;
    Long officeId;
    Long layerLevel;
    Long customLayer;
    Long officeOrigin;
    String mediumOfSubmission;
    String grievanceType;
    Long selfMotivated;
    Date createdAt;
    Date closedAt;

    public ComplainHistory(Object[] model) {
        if (model != null) {
            if (Utils.valueExists(model, 0)) {
                this.complainId = Utils.getLongValue(model[0]);
            }
            if (Utils.valueExists(model, 1)) {
                this.trackingNumber = (String) model[1];
            }
            if (Utils.valueExists(model, 2)) {
                this.currentStatus = (String) model[2];
            }
            if (Utils.valueExists(model, 3)) {
                this.officeId = Utils.getLongValue(model[3]);
            }
            if (Utils.valueExists(model, 4)) {
                this.layerLevel = Utils.getLongValue(model[4]);
            }
            if (Utils.valueExists(model, 5)) {
                this.customLayer = Utils.getLongValue(model[5]);
            }
            if (Utils.valueExists(model, 6)) {
                this.officeOrigin = Utils.getLongValue(model[6]);
            }
            if (Utils.valueExists(model, 7)) {
                this.mediumOfSubmission = (String) model[7];
            }
            if (Utils.valueExists(model, 8)) {
                this.grievanceType = (String) model[8];
            }
            if (Utils.valueExists(model, 9)) {
                this.selfMotivated = Utils.getLongValue(model[9]);
            }
            // Handle createdAt field and set time to 00:00:00
            if (Utils.valueExists(model, 10)) {
                this.createdAt = CalendarUtil.truncateDate((Date) model[10]);
            }
            // Handle closedAt field and set time to 00:00:00
            if (Utils.valueExists(model, 11)) {
                this.closedAt = CalendarUtil.truncateDate((Date) model[11]);
            }
        }
    }

    public com.kit.grs.model.ComplainHistory getComplainHistory() {
        com.kit.grs.model.ComplainHistory complainHistoryEO = new com.kit.grs.model.ComplainHistory();
        complainHistoryEO.setComplainId(this.complainId);
        complainHistoryEO.setTrackingNumber(this.trackingNumber);
        complainHistoryEO.setCurrentStatus(this.currentStatus);
        complainHistoryEO.setOfficeId(this.officeId);
        complainHistoryEO.setLayerLevel(this.layerLevel);
        complainHistoryEO.setCustomLayer(this.customLayer);
        complainHistoryEO.setOfficeOrigin(this.officeOrigin);
        complainHistoryEO.setMediumOfSubmission(this.mediumOfSubmission);
        complainHistoryEO.setGrievanceType(this.grievanceType);
        complainHistoryEO.setSelfMotivated(this.selfMotivated);
        complainHistoryEO.setCreatedAt(this.createdAt);
        complainHistoryEO.setClosedAt(this.closedAt);
        return complainHistoryEO;
    }

}
