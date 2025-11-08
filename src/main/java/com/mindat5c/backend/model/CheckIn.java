package com.mindat5c.backend.model;

public class CheckIn {
    private Integer sleep;
    private Integer nutrition;
    private Integer stress;
    private Integer activity;
    private String timestamp;

    public CheckIn() {}

    public CheckIn(Integer sleep, Integer nutrition, Integer stress, Integer activity, String timestamp) {
        this.sleep = sleep;
        this.nutrition = nutrition;
        this.stress = stress;
        this.activity = activity;
        this.timestamp = timestamp;
    }

    public Integer getSleep() { return sleep; }
    public void setSleep(Integer sleep) { this.sleep = sleep; }

    public Integer getNutrition() { return nutrition; }
    public void setNutrition(Integer nutrition) { this.nutrition = nutrition; }

    public Integer getStress() { return stress; }
    public void setStress(Integer stress) { this.stress = stress; }

    public Integer getActivity() { return activity; }
    public void setActivity(Integer activity) { this.activity = activity; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
