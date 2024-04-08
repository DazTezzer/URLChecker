package ru.project.Urlchecker.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.project.Urlchecker.tables.UrlInfo;
import java.util.List;
import java.util.Map;

@Controller
@ComponentScan("ru.project.Urlchecker")
public class WebController {
    private final RestTemplate restTemplate;
    public WebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final String REST_API_URL = "http://localhost:8081";


    @GetMapping("/home")
    public String urlsInfoList(Model model) {
        ResponseEntity<List<UrlInfo>> responseEntity = restTemplate.exchange(
                REST_API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UrlInfo>>() {}
        );

        List<UrlInfo> urlInfoList = responseEntity.getBody();
        model.addAttribute("urls", urlInfoList);
        return "home";
    }
    @PostMapping("/addurls")
    public String urlsAdd(@RequestParam("url") String url, @RequestParam("response_period") Integer response_period) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    REST_API_URL + "/status?url=" + url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> result = responseEntity.getBody();
            if (result != null) {
                Integer delay = (Integer) result.get("delay");
                Boolean isValid = (Boolean) result.get("isValid");

                if (delay == null || isValid == null) {
                    delay = 0;
                    isValid = false;
                }
                UrlInfo urlInfo = new UrlInfo(url, isValid, delay, response_period);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<UrlInfo> requestEntity = new HttpEntity<>(urlInfo, headers);
                restTemplate.exchange(
                        REST_API_URL,
                        HttpMethod.PUT,
                        requestEntity,
                        Void.class
                );
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                if (e.getResponseBodyAsString().contains("Failed to connect to the URL or read response")) {
                    return "redirect:/home";
                } else if (e.getResponseBodyAsString().contains("Invalid URL format")) {
                    return "redirect:/home";
                } else if (e.getResponseBodyAsString().contains("IO Exception occurred")){
                    return "redirect:/home";
                }
            }

        } catch (Exception e) {
            return "redirect:/home";
        }

        return "redirect:/home";
    }
    @PostMapping("/deleteurls")
    public String urlsDelete(@RequestParam("id") String id) {
        long idAsLong = Long.parseLong(id);
        restTemplate.exchange(
                REST_API_URL +"/"+ idAsLong,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        return "redirect:/home";
    }
    @PostMapping("/home")
    public String refresh() {
        return "redirect:/home";
    }
}
