package ru.project.Urlchecker.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class UrlInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;
    private boolean status;
    private int delay;
    private int response_period;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlInfo urlInfo)) return false;
        return isStatus() == urlInfo.isStatus() && getDelay() == urlInfo.getDelay() && getResponse_period() == urlInfo.getResponse_period() && Objects.equals(getId(), urlInfo.getId()) && Objects.equals(getUrl(), urlInfo.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUrl(), isStatus(), getDelay(), getResponse_period());
    }

    public int getResponse_period() {
        return response_period;
    }

    public void setResponse_period(int response_period) {
        this.response_period = response_period;
    }

    public UrlInfo() {
    }

    public UrlInfo(String url, boolean status, int delay,int response_period) {
        this.url = url;
        this.status = status;
        this.delay = delay;
        this.response_period = response_period;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UrlInfo;
    }

}
