package com.mindat5c.backend.model;

import java.util.List;

public class CheckInResponse {
    private boolean ok;
    private String message;
    private List<Resource> resources;

    public CheckInResponse() {}

    public CheckInResponse(boolean ok, String message, List<Resource> resources) {
        this.ok = ok;
        this.message = message;
        this.resources = resources;
    }

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Resource> getResources() { return resources; }
    public void setResources(List<Resource> resources) { this.resources = resources; }
}
