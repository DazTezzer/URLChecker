package ru.project.Urlchecker.dto;

import java.util.Objects;

public class UrlInfoDTO {
    private String url;
    private boolean status;
    private int delay;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlInfoDTO that)) return false;
        return isStatus() == that.isStatus() && getDelay() == that.getDelay() && Objects.equals(getUrl(), that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), isStatus(), getDelay());
    }
}
