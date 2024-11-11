package org.intelehealth.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartbeatApiRequest {

    @SerializedName("userUuid")
    @Expose
    private String userUuid;

    @SerializedName("currentTimestamp")
    @Expose
    private long currentTimestamp;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("androidVersion")
    @Expose
    private String androidVersion;

    @SerializedName("device")
    @Expose
    private String device;

    @SerializedName("deviceModel")
    @Expose
    private String deviceModel;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lastSyncTimestamp")
    @Expose
    private Long lastSyncTimestamp;

    @SerializedName("lastActivity")
    @Expose
    private String lastActivity;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("village")
    @Expose
    private String village;

    @SerializedName("secondaryVillage")
    @Expose
    private String secondaryVillage;

    @SerializedName("sanch")
    @Expose
    private String sanch;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public long getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLastSyncTimestamp() {
        return lastSyncTimestamp;
    }

    public void setLastSyncTimestamp(Long lastSyncTimestamp) {
        this.lastSyncTimestamp = lastSyncTimestamp;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSecondaryVillage() {
        return secondaryVillage;
    }

    public void setSecondaryVillage(String secondaryVillage) {
        this.secondaryVillage = secondaryVillage;
    }

    public String getSanch() {
        return sanch;
    }

    public void setSanch(String sanch) {
        this.sanch = sanch;
    }
}
