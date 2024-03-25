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
    @GetMapping()
    public ResponseEntity<List<UrlInfo>> readAll() {
        return new ResponseEntity<>(urlInfoService.readAll(), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<UrlInfo> update(@RequestBody UrlInfo urlInfo) {
        return new ResponseEntity<>(urlInfoService.update(urlInfo), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id){
        try {
            urlInfoService.delete(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkIfUrl(@RequestParam String url) {
        return ResponseEntity.ok(urlInfoService.status(url).getBody());
    }
}
