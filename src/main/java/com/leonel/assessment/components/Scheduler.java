package com.leonel.assessment.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.leonel.assessment.AssessmentApplication;

@Component
public class Scheduler {
	
	@Autowired
	private MetricsService metricsService;
	
	@Value("${request}")
	private String request;
	
    private static final Logger log = LoggerFactory.getLogger(AssessmentApplication.class);
    
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
    
    RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
	
	@Scheduled(fixedRate=100)
	public void checkHealth() {
		try {
			ResponseEntity<String> result = restTemplate.getForEntity(request, String.class);
			metricsService.increaseCount(result.getStatusCodeValue());
		} catch (HttpServerErrorException e) {
			metricsService.increaseCount(e.getRawStatusCode());
			log.warn("The service " + request + " returned an error.");
		} catch (ResourceAccessException e) {
			metricsService.increaseCount(403);
			log.error("The service " + request + " is down.");
		}

		log.info(metricsService.getStatusMetric().toString());
		Map<Integer, Integer> statusMetric = metricsService.getStatusMetric();
		int numberOf500 = statusMetric.get(500).intValue();
		int numberOf200 = statusMetric.get(200).intValue();
		float relation = (float) ( (float) numberOf500 / (float) (numberOf500+numberOf200) )*100;
		log.info("The availability is rated in "+ (100f - relation) + "%");
	}

}
