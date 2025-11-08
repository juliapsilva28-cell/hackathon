package com.mindat5c.backend.model;

public class Stats {
    private int count;
    private Double avgSleep;
    private Double avgNutrition;
    private Double avgStress;
    private Double avgActivity;
    private int communityPulse;

    public Stats() {}

    public Stats(int count, Double avgSleep, Double avgNutrition,
                 Double avgStress, Double avgActivity, int communityPulse) {
        this.count = count;
        this.avgSleep = avgSleep;
        this.avgNutrition = avgNutrition;
        this.avgStress = avgStress;
        this.avgActivity = avgActivity;
        this.communityPulse = communityPulse;
    }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public Double getAvgSleep() { return avgSleep; }
    public void setAvgSleep(Double avgSleep) { this.avgSleep = avgSleep; }

    public Double getAvgNutrition() { return avgNutrition; }
    public void setAvgNutrition(Double avgNutrition) { this.avgNutrition = avgNutrition; }

    public Double getAvgStress() { return avgStress; }
    public void setAvgStress(Double avgStress) { this.avgStress = avgStress; }

    public Double getAvgActivity() { return avgActivity; }
    public void setAvgActivity(Double avgActivity) { this.avgActivity = avgActivity; }

    public int getCommunityPulse() { return communityPulse; }
    public void setCommunityPulse(int communityPulse) { this.communityPulse = communityPulse; }
}
