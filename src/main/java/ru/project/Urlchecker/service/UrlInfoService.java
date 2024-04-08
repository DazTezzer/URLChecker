package ru.project.Urlchecker.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.project.Urlchecker.dto.UrlInfoDTO;
import ru.project.Urlchecker.repository.UrlInfoRepository;
import ru.project.Urlchecker.tables.UrlInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UrlInfoService {
    private final UrlInfoRepository urlInfoRepository;

    public UrlInfoService(UrlInfoRepository urlInfoRepository) {
        this.urlInfoRepository = urlInfoRepository;
    }
    @NotNull
    @Valid
    public UrlInfo create(UrlInfoDTO dto) {
        return urlInfoRepository.save(new UrlInfo(dto.getUrl(),dto.isStatus(),dto.getDelay(),dto.getResponse_period())) ;
    }
    @NotNull
    @Valid
    public List<UrlInfo> readAll(){
        return urlInfoRepository.findAll();
    }
    @NotNull
    @Valid
    public UrlInfo update( UrlInfo urlInfo) {
        if (!isValidUrl(urlInfo.getUrl())) {
            throw new IllegalArgumentException("Invalid URL provided");
        }

        UrlInfo existingUrlInfo = urlInfoRepository.findByUrl(urlInfo.getUrl());

        if (existingUrlInfo != null) {
            existingUrlInfo.setStatus(urlInfo.isStatus());
            existingUrlInfo.setDelay(urlInfo.getDelay());
            existingUrlInfo.setResponse_period(urlInfo.getResponse_period());

            return urlInfoRepository.save(existingUrlInfo);
        } else {
            return urlInfoRepository.save(urlInfo);
        }
    }
    private boolean isValidUrl(@NotEmpty String url) {
        return url != null && url.matches("^(http|https)://.*$");
    }
    @Valid
    public void delete(Long id) {
        urlInfoRepository.deleteById(id);
    }
    public ResponseEntity<Map<String, Object>> status(String url) {
        try {
            long startTime = System.currentTimeMillis();
            URL requestUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("HEAD");
            con.disconnect();
            int responseCode = con.getResponseCode();
            long endTime = System.currentTimeMillis();
            long delay = endTime - startTime;
            boolean isValid = responseCode >= 200 && responseCode <= 399;
            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            result.put("delay", delay);
            HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(result);
        } catch (MalformedURLException e) {
            return handleExceptionResponse(e, "Invalid URL format");
        } catch (IOException e) {
            return handleExceptionResponse(e, "Failed to connect to the URL or read response");
        } catch (Exception e) {
            return handleExceptionResponse(e, "IO Exception occurred");
        }
    }
    public ResponseEntity<Map<String, Object>> handleExceptionResponse(Exception e, String errorMessage) {
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("error", errorMessage);
        return ResponseEntity.badRequest().body(errorResult);
    }
}
