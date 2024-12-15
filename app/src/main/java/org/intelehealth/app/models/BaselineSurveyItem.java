package org.intelehealth.app.models;

public class BaselineSurveyItem {

    private String id;
    private String name;

    public String getOpenMRSID() {
        return id;
    }

    public void setOpenMRSID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
