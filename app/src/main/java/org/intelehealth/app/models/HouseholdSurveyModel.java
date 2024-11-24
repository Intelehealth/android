package org.intelehealth.app.models;

public class HouseholdSurveyModel {
    String houseStructure;

    public String getHouseStructure() {
        return houseStructure;
    }

    public void setHouseStructure(String houseStructure) {
        this.houseStructure = houseStructure;
    }

    public String getResultOfVisit() {
        return resultOfVisit;
    }

    public void setResultOfVisit(String resultOfVisit) {
        this.resultOfVisit = resultOfVisit;
    }

    public String getNamePrimaryRespondent() {
        return namePrimaryRespondent;
    }

    public void setNamePrimaryRespondent(String namePrimaryRespondent) {
        this.namePrimaryRespondent = namePrimaryRespondent;
    }

    public String getReportDateOfSurveyStarted() {
        return reportDateOfSurveyStarted;
    }

    public void setReportDateOfSurveyStarted(String reportDateOfSurveyStarted) {
        this.reportDateOfSurveyStarted = reportDateOfSurveyStarted;
    }

    public String getHouseholdNumberOfSurvey() {
        return householdNumberOfSurvey;
    }

    public void setHouseholdNumberOfSurvey(String householdNumberOfSurvey) {
        this.householdNumberOfSurvey = householdNumberOfSurvey;
    }

    String resultOfVisit;
    String namePrimaryRespondent;
    String reportDateOfSurveyStarted;
    String householdNumberOfSurvey;

}
