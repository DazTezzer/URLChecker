package ru.project.Urlchecker.service;

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

    public UrlInfo create(UrlInfoDTO dto) {
        return urlInfoRepository.save(new UrlInfo(dto.getUrl(),dto.getStatus(),dto.getDelay(),dto.getResponse_period())) ;
    }
    public List<UrlInfo> readAll(){
        return urlInfoRepository.findAll();
    }
    public UrlInfo update(UrlInfo urlInfo) {
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
    public void delete(Long id) {
        urlInfoRepository.deleteById(id);
    }
    public ResponseEntity<Map<String, Object>> status(String url) {
        long startTime = System.currentTimeMillis();

        try {
            URL requestUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            long endTime = System.currentTimeMillis();
            long delay = endTime - startTime;

            boolean isValid = responseCode >= 200 && responseCode <= 399;

            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            result.put("delay", delay);
            con.disconnect();
            HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(result);
        } catch (MalformedURLException e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "Invalid URL format");
            return ResponseEntity.badRequest().body(errorResult);
        } catch (IOException e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "Failed to connect to the URL or read response");
            return ResponseEntity.badRequest().body(errorResult);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "IO Exception occurred");
            return ResponseEntity.badRequest().body(errorResult);
        }
    }
}
