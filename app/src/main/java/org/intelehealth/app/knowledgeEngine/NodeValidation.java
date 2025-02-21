package org.intelehealth.app.knowledgeEngine;

public class NodeValidation {
    //{
    //                          "type": "male",
    //                          "min": 13,
    //                          "max": 17
    //                        }

    private String type;
    private double min;
    private double max;
    private String checkValues;

    public NodeValidation(String type, double min, double max, String checkValues) {
        this.type = type;
        this.min = min;
        this.max = max;
        this.checkValues = checkValues;
    }

    // generate getters and setters for the above fields
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }


    public String getCheckValues() {
        return checkValues;
    }

    public void setCheckValues(String checkValues) {
        this.checkValues = checkValues;
    }
}
