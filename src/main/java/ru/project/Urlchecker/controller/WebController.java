package ru.project.Urlchecker.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.project.Urlchecker.tables.UrlInfo;

import java.util.List;

@Controller
@ComponentScan("ru.project.Urlchecker")
@RequestMapping("/home")
public class WebController {
    private final RestTemplate restTemplate;
    public WebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final String REST_API_URL = "http://localhost:8081";



    @GetMapping()
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

    @PostMapping()
    public String refresh() {
        return "redirect:/home";
    }
}
