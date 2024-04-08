package ru.project.Urlchecker.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;


@Data
public class UrlInfoDTO {

    @NotNull
    private String url;

    private boolean status;

    @PositiveOrZero
    private int delay;

    @Positive
    private int response_period;
}
