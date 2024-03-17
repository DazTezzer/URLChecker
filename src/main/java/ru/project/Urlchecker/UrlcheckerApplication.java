package ru.project.Urlchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UrlcheckerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UrlcheckerApplication.class, args);
		RestTemplate restTemplateResponsePeriod = context.getBean("restTemplateResponsePeriod", RestTemplate.class);

		UrlsResponsePeriodThread urlsResponsePeriodThread = new UrlsResponsePeriodThread(restTemplateResponsePeriod);
		urlsResponsePeriodThread.start();
	}

}

