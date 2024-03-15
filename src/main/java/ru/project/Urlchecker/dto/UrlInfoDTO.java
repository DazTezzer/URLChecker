package ru.project.Urlchecker.dto;

import java.util.Objects;

public class UrlInfoDTO {
    private String url;
    private boolean status;
    private int delay;
    private int response_period;

    public int getResponse_period() {
        return response_period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlInfoDTO that)) return false;
        return isStatus() == that.isStatus() && getDelay() == that.getDelay() && getResponse_period() == that.getResponse_period() && Objects.equals(getUrl(), that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), isStatus(), getDelay(), getResponse_period());
    }

    public void setResponse_period(int response_period) {
        this.response_period = response_period;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
