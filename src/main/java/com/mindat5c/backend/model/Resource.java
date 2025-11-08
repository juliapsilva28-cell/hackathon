package com.mindat5c.backend.model;

public class Resource {
    private String label;
    private String url;
    private boolean priority;
    private String reason;

    public Resource() {}

    public Resource(String label, String url, boolean priority, String reason) {
        this.label = label;
        this.url = url;
        this.priority = priority;
        this.reason = reason;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean isPriority() { return priority; }
    public void setPriority(boolean priority) { this.priority = priority; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
