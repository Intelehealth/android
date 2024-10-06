package org.intelehealth.app.models.locationAttributes.push;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationAttributes {

    @SerializedName("secondaryVillageId")
    @Expose
    private String secondaryVillageId = null;

    @SerializedName("attributeType")
    @Expose
    private String attributeType;

    @SerializedName("value")
    @Expose
    private String value;

    public String getSecondaryVillageId() {
        return secondaryVillageId;
    }

    public void setSecondaryVillageId(String secondaryVillageId) {
        this.secondaryVillageId = secondaryVillageId;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
