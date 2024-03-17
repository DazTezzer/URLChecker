package ru.project.Urlchecker.controller;

import jakarta.persistence.GeneratedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.Urlchecker.dto.UrlInfoDTO;
import ru.project.Urlchecker.service.UrlInfoService;
import ru.project.Urlchecker.tables.UrlInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UrlInfoController {
    private final UrlInfoService urlInfoService;

    public UrlInfoController(UrlInfoService urlInfoService) {
        this.urlInfoService = urlInfoService;
    }
    @PostMapping
    public ResponseEntity<UrlInfo> create(@RequestBody UrlInfoDTO dto) {
        return new ResponseEntity<>(urlInfoService.create(dto), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<UrlInfo>> readAll() {
        return new ResponseEntity<>(urlInfoService.readAll(), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<UrlInfo> update(@RequestBody UrlInfo urlInfo) {
        return new ResponseEntity<>(urlInfoService.update(urlInfo), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id){
        urlInfoService.delete(id);
        return HttpStatus.OK;
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkIfUrl(@RequestParam String url) {
        long startTime = System.currentTimeMillis();
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");
            con.connect();
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
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "OUT OF SHAPE URL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        } catch (IOException e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "IO Exception occurred");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
    }
}
