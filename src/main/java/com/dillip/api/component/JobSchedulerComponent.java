package com.dillip.api.component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobSchedulerComponent {

	private final HttpClient httpClient;

	@Value("${render.base.url}")
	private String renderBaseUrl;

//	@Scheduled(cron = "0 * * * * *")
	public void callRenderStartApi() throws IOException, InterruptedException {
		log.info("Running Scheduler -- JobSchedulerComponent::callRenderStartApi()");
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).GET().uri(URI.create(renderBaseUrl))
				.build();

		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		log.info("########## Response from render API = {} ##########", httpResponse.body());
	}
}
