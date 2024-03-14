package ru.project.Urlchecker.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Arrays;

@Entity
public class UrlInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;
    private boolean status;
    private int delay;

    public UrlInfo() {
    }

    public UrlInfo(String url, boolean status, int delay) {
        this.url = url;
        this.status = status;
        this.delay = delay;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlInfo other = (UrlInfo) o;

        if (status != other.status) return false;
        if (delay != other.delay) return false;
        if (id != null ? !id.equals(other.id) : other.id != null) return false;
        if (url != null ? !url.equals(other.url) : other.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        result = PRIME * result + (url == null ? 0 : url.hashCode());
        result = PRIME * result + Boolean.hashCode(status);
        result = PRIME * result + Integer.hashCode(delay);
        return result;
    }
}
