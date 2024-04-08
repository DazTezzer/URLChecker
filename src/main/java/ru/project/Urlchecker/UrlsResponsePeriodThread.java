package ru.project.Urlchecker;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.project.Urlchecker.tables.UrlInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UrlsResponsePeriodThread extends Thread {
    private static final String REST_API_URL = "http://localhost:8081";
    private final RestTemplate restTemplateResponsePeriod;
    private static final int THREAD_POOL_SIZE = 10;
    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    public UrlsResponsePeriodThread(RestTemplate restTemplateResponsePeriod) {
        this.restTemplateResponsePeriod = restTemplateResponsePeriod;
    }
    @Override
    public void run() {
        System.out.println("\u001B[32m-------------UrlsResponsePeriodThread started-------------\u001B[0m");
        Map<Long, Integer> QueueMap = new HashMap<>();
        while (!Thread.interrupted()) {
            ResponseEntity<List<UrlInfo>> responseEntity = restTemplateResponsePeriod.exchange(
                    REST_API_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UrlInfo>>() {
                    }
            );

            List<UrlInfo> urlInfoList = responseEntity.getBody();
            assert urlInfoList != null;
            Map<Long, UrlInfo> HelpMap = new HashMap<>();
            for (UrlInfo urlInfo : urlInfoList) {
                if (!HelpMap.containsKey(urlInfo.getId())) {
                    HelpMap.put(urlInfo.getId(),urlInfo);
                }
                if (!QueueMap.containsKey(urlInfo.getId())) {
                    QueueMap.put(urlInfo.getId(), urlInfo.getResponse_period());
                }
            }
            Iterator<Map.Entry<Long, Integer>> iterator = QueueMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Integer> entry = iterator.next();
                UrlInfo help = HelpMap.get(entry.getKey());
                if (help == null) {
                    System.out.println("UrlInfo object is null for key: " + entry.getKey());
                    iterator.remove();
                } else {
                    if (entry.getValue() == 1) {
                        checkUrlAvailability(help);
                        QueueMap.put(entry.getKey(), help.getResponse_period());
                    } else {
                        QueueMap.put(entry.getKey(), entry.getValue() - 1);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void checkUrlAvailability(UrlInfo urlInfo) {
        if (urlInfo == null) {
            return;
        }
        int intDelay;
        try {
            long startTime = System.currentTimeMillis();
            URL requestUrl = new URL(urlInfo.getUrl());
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("HEAD");
            con.disconnect();
            int responseCode = con.getResponseCode();
            long endTime = System.currentTimeMillis();
            long delay = endTime - startTime;
            intDelay = Math.toIntExact(delay);

            if (responseCode >= 200 && responseCode <= 399) {
                putUrls(urlInfo, true, intDelay);
                System.out.println("\u001B[32m" + urlInfo.getUrl() + " is reachable  delay = " + intDelay + "\u001B[0m");
            } else {
                putUrls(urlInfo, false, intDelay);
                System.out.println("\u001B[31m" + urlInfo.getUrl() + " is unreachable delay = " + intDelay + "\u001B[0m");
            }
        } catch (Exception e) {
            putUrls(urlInfo, false, 0);
            System.out.println("\u001B[31m" + urlInfo.getUrl() + " is unreachable delay = " + 0 + "\u001B[0m");
        }
    }
    public void putUrls(UrlInfo urlInfo,boolean status,int intDelay )
    {
        urlInfo.setStatus(status);
        urlInfo.setDelay(intDelay);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UrlInfo> requestEntity = new HttpEntity<>(urlInfo, headers);
        executorService.submit(() -> {
            restTemplateResponsePeriod.exchange(
                    REST_API_URL,
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
        });
    }
}
