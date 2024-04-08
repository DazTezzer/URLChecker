package ru.project.Urlchecker.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String url;

    private boolean status;

    @PositiveOrZero
    private int delay;

    @Positive
    private int response_period;

    public UrlInfo(@NotNull String url, boolean status, int delay, int response_period) {
        this.url = url;
        this.status = status;
        this.delay = delay;
        this.response_period = response_period;
    }
}
